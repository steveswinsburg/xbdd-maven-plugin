package io.github.steveswinsburg.xbdd;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.utils.io.FileUtils;

import lombok.Getter;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class SendTestResultsToXbddMojo extends AbstractMojo {

	private static final String XBDD_HOST = "host";
	private static final String XBDD_USERNAME = "username";
	private static final String XBDD_PASSWORD = "password";
	private static final String XBDD_PROJECT_KEY = "projectKey";
	private static final String XBDD_PROJECT_VERSION = "projectVersion";
	private static final String XBDD_PROJECT_BUILD_NUMBER = "buildNumber";
	private static final String XBDD_REPORT = "report";

	/**
	 * The host URL of XBDD
	 */
	@Getter
	@Parameter(property = XBDD_HOST)
	private String host;

	/**
	 * The username to use to send reports to XBDD
	 */
	@Getter
	@Parameter(property = XBDD_USERNAME)
	private String username;

	/**
	 * The password to use to send reports to XBDD
	 */
	@Getter
	@Parameter(property = XBDD_PASSWORD)
	private String password;

	/**
	 * The project key in XBDD
	 */
	@Getter
	@Parameter(property = XBDD_PROJECT_KEY)
	private String projectKey;

	/**
	 * The project version in XBDD
	 */
	@Getter
	@Parameter(property = XBDD_PROJECT_VERSION)
	private String projectVersion;

	/**
	 * The build number to attach the report to
	 */
	@Getter
	@Parameter(property = XBDD_PROJECT_BUILD_NUMBER)
	private String buildNumber;

	@Getter
	@Parameter(property = XBDD_REPORT)
	private List<String> reports;

	/**
	 * The project that is running this plugin
	 */
	@Getter
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		ensureBuildNumber();
		ensureProjectKey();
		ensureProjectVersion();

		cleanReports();

		validate(this.host, XBDD_HOST);
		validate(this.username, XBDD_USERNAME);
		validate(this.password, XBDD_PASSWORD);
		validate(this.projectKey, XBDD_PROJECT_KEY);
		validate(this.projectVersion, XBDD_PROJECT_VERSION);

		if (anyEmpty(this.host, this.username, this.password, this.projectKey, this.projectVersion)) {
			getLog().error("Required configuration missing. Aborting upload.");
			return;
		}

		if (this.reports.isEmpty()) {
			getLog().error("No reports to upload. Aborting upload.");
			return;
		}

		try {
			upload();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | AuthenticationException | IOException e) {
			getLog().error(String.format("Error uploading report: %s %s", e.getClass().getName(), e.getMessage()));
		}
	}

	/**
	 * Check if a property value is set. If not, log it. We do not fail the build.
	 *
	 * @param property the property to check
	 * @param key the actual key that implementers are meant to set, for logging purposes only
	 */
	private void validate(final String property, final String key) {
		if (StringUtils.isBlank(property)) {
			getLog().error(String.format("'%s' was not set. Cannot upload to XBDD.", key));
		}
	}

	/**
	 * Check if any of the provided elements are empty
	 *
	 * @param elements elements to check for emptiness
	 * @return
	 */
	private boolean anyEmpty(final String... elements) {
		return Arrays.asList(elements).stream().anyMatch(e -> StringUtils.isBlank(e));
	}

	/**
	 * Do the upload work
	 *
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws AuthenticationException
	 * @throws IOException
	 */
	protected void upload()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, AuthenticationException, IOException {

		final String url = getUrl();
		getLog().info(String.format("Uploading to: %s ", url));

		final UsernamePasswordCredentials creds = new UsernamePasswordCredentials(this.username, this.password);

		// ignore certificates
		final SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(null, (x509CertChain, authType) -> true)
				.build();

		final CloseableHttpClient httpClient = HttpClientBuilder
				.create()
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.build();

		final HttpPut request = new HttpPut(url);
		request.addHeader(new BasicScheme().authenticate(creds, request, null));
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		for(String r: reports) {
		
			getLog().info(String.format("Uploading report: %s ", r));
			StringEntity entity = null;

			// check file exists
			if(!FileUtils.fileExists(r)) {
				getLog().error(String.format("Report does not exist. Skipping: %s", r));
				continue;
			}
			
			// read the file
			try {
				final String json = FileUtils.fileRead(r);
				entity = new StringEntity(json);
			} catch (final IOException e) {
				getLog().error(String.format("Cannot upload: %s", r));
				continue;
			}

			request.setEntity(entity);
			
			// upload the file
			try {
				final CloseableHttpResponse response = httpClient.execute(request);
				
				int statusCode = response.getStatusLine().getStatusCode();
				
				// check response
				if(statusCode == 200) {
					getLog().info(String.format("XBDD upload result: %d %s", response.getStatusLine().getStatusCode(),
							response.getStatusLine().getReasonPhrase()));
				} else {
					getLog().error(String.format("Error uploading to XBDD: %d %s", response.getStatusLine().getStatusCode(),
							response.getStatusLine().getReasonPhrase()));
				}

			} catch (final IOException e) {
				e.printStackTrace();
			}

		}

		httpClient.close();

	}

	/**
	 * Build the URL for report upload
	 *
	 * @return the url
	 */
	private String getUrl() {
		final StringBuilder sb = new StringBuilder();
		sb.append(slashify(this.host));
		sb.append("rest/reports/");
		sb.append(slashify(this.projectKey));
		sb.append(slashify(this.projectVersion));
		sb.append(slashify(this.buildNumber));
		return sb.toString();
	}

	/**
	 * Ensure the string ends with a /
	 *
	 * @param s the string
	 * @return the string with a /
	 */
	private String slashify(final String s) {
		return StringUtils.appendIfMissing(s, "/");
	}

	private void ensureBuildNumber() {
		if (StringUtils.isBlank(this.buildNumber)) {
			this.buildNumber = String.valueOf(Instant.now().toEpochMilli());
		}
	}

	private void ensureProjectKey() {
		if (StringUtils.isBlank(this.projectKey)) {
			this.projectKey = this.project.getArtifactId();
		}
	}

	private void ensureProjectVersion() {
		if (StringUtils.isBlank(this.projectVersion)) {
			this.projectVersion = this.project.getVersion();
		}

		cleanVersion();
	}

	/**
	 * XBDD has a requirement for major.minor.servicepack versioning, no exceptions. Ensure the version is in that format.
	 */
	private void cleanVersion() {

		final List<String> parts = new LinkedList<>(Arrays.asList(StringUtils.split(this.projectVersion, '.')));

		// remove anything that doesn't look like a number
		parts.removeIf(e -> !NumberUtils.isParsable(e));

		if (parts.isEmpty()) {
			throw new IllegalArgumentException("Version was invalid: " + this.projectVersion);
		}

		// pad it out to 3 if necessary
		while (parts.size() < 3) {
			parts.add("0");
		}

		// retain maximum of 3 elements
		if (parts.size() > 3) {
			parts.subList(2, 3).clear();
		}

		getLog().info(String.format("New version: %s", parts));

		this.projectVersion = String.join(".", parts);
	}

	/**
	 * Remove empty report paths
	 */
	private void cleanReports() {
		if (this.reports != null) {
			this.reports = this.reports.stream().filter(e -> StringUtils.isNotBlank(e)).collect(Collectors.toList());
		}
	}

}
