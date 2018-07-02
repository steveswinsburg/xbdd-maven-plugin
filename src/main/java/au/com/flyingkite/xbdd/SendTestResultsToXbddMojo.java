package au.com.flyingkite.xbdd;

import java.io.IOException;
import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class SendTestResultsToXbddMojo extends AbstractMojo {

	private static final String XBDD_HOST = "xbdd.host";
	private static final String XBDD_USERNAME = "xbdd.username";
	private static final String XBDD_PASSWORD = "xbdd.password";
	private static final String XBDD_PROJECT_KEY = "xbdd.projectkey";
	private static final String XBDD_PROJECT_VERSION = "xbdd.projectversion";
	private static final String XBDD_PROJECT_BUILD_NUMBER = "xbdd.buildNumber";

	/**
	 * The host URL of XBDD
	 */
	@Parameter(property = XBDD_HOST)
	private String host;

	/**
	 * The username to use to send reports to XBDD
	 */
	@Parameter(property = XBDD_USERNAME)
	private String username;

	/**
	 * The password to use to send reports to XBDD
	 */
	@Parameter(property = XBDD_PASSWORD)
	private String password;

	/**
	 * The project key in XBDD
	 */
	@Parameter(property = XBDD_PROJECT_KEY, defaultValue = "${project.artifactId}")
	private String projectKey;

	/**
	 * The project version in XBDD
	 */
	@Parameter(property = XBDD_PROJECT_VERSION, defaultValue = "${project.version}")
	private String projectVersion;

	/**
	 * The build number to attach the report to
	 */
	@Parameter(property = XBDD_PROJECT_BUILD_NUMBER)
	private String buildNumber;

	/*
	 * <xbdd.host>https://xbdd/</xbdd.host> <xbdd.username>xbdd</xbdd.username> <xbdd.password>xbdd</xbdd.password>
	 * <xbdd.projectkey>test1</xbdd.projectkey> <xbdd.projectversion>test1</xbdd.projectversion> <xbdd.buildnumber>1</xbdd.buildnumber>
	 */

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		if (StringUtils.isBlank(this.buildNumber)) {
			this.buildNumber = String.valueOf(Instant.now().toEpochMilli());
		}

		validate(this.host, XBDD_HOST);
		validate(this.username, XBDD_USERNAME);
		validate(this.password, XBDD_PASSWORD);

		getLog().info("Hello, world.");

		getLog().info("projectKey:" + this.projectKey);

		getLog().info("buildNumber: " + this.buildNumber);

	}

	/**
	 * Check if a property value is set. If not, log it. We do not fail the build.
	 *
	 * @param property the property to check
	 * @param key the actual key that implementers are meant to set, for logging purposes only
	 */
	private void validate(final String property, final String key) {
		if (StringUtils.isBlank(property)) {
			getLog().error(String.format("%s was not set. Cannot upload to XBDD.", key));
		}
	}

	private void upload() {

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.username, this.password));

		final CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		final HttpPut request = new HttpPut(getUrl());

		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// builder.add

		try {
			final CloseableHttpResponse response = httpClient.execute(request);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Build the URL for report upload
	 *
	 * @return the url
	 */
	private String getUrl() {
		// https://host/xbdd/rest/reports/GDM/${gdm.version}/${bamboo.build.number}
		final StringBuilder sb = new StringBuilder();
		sb.append(slashify(this.host));
		sb.append("xbdd/rest/reports/");
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

}
