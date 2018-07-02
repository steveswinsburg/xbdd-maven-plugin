package au.com.flyingkite.xbdd;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo( name = "upload", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class SendTestResultsToXbddMojo extends AbstractMojo {

	private static final String XBDD_HOST = "xbdd.host";
	private static final String XBDD_USERNAME = "xbdd.username";
	private static final String XBDD_PASSWORD = "xbdd.password";

	
	@Parameter( property = XBDD_HOST)
	private String host;

	@Parameter( property = XBDD_USERNAME)
	private String username;
	
	@Parameter( property = XBDD_PASSWORD)
	private String password;
	
	@Parameter( property = "xbdd.projectkey", defaultValue="${project.artifactId}")
	private String projectKey;
	
	@Parameter( property = "xbdd.projectversion", defaultValue="${project.version}")
	private String projectVersion;
	
	@Parameter( property = "xbdd.buildNumber", defaultValue="${project.version}")
	private String buildNumber;
	
	
	/*
	<xbdd.host>https://xbdd/</xbdd.host>
		<xbdd.username>xbdd</xbdd.username>
		<xbdd.password>xbdd</xbdd.password>
		<xbdd.projectkey>test1</xbdd.projectkey>
		<xbdd.projectversion>test1</xbdd.projectversion>
		<xbdd.buildnumber>1</xbdd.buildnumber>
	*/


	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		if(StringUtils.isBlank(buildNumber)) {
			buildNumber = String.valueOf(Instant.now().toEpochMilli());
		}
		
		validate(host, XBDD_HOST);
		validate(username, XBDD_USERNAME);
		validate(password, XBDD_PASSWORD);

			
		getLog().info( "Hello, world." );
		
		getLog().info("projectKey:" + projectKey);
		
		getLog().info("buildNumber: " + buildNumber);

	}
	
	/**
	 * Check if a property value is set. If not, log it. We do not fail the build.
	 * @param property the property to check
	 * @param key the actual key that implementers are meant to set, for logging purposes only
	 */
	private void validate(String property, String key) {
		if(StringUtils.isBlank(property)) {
			getLog().error(String.format("%s was not set. Cannot upload to XBDD.", key));
		}
	}
	
	private void upload() {
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.username, this.password));
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
		HttpPut request = new HttpPut(getUrl());
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		//builder.add
		
		try {
			CloseableHttpResponse response = httpClient.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/**
	 * Build the URL for report upload
	 * @return the url
	 */
	private String getUrl() {
		//https://host/xbdd/rest/reports/GDM/${gdm.version}/${bamboo.build.number}
		StringBuilder sb = new StringBuilder();
		sb.append(slashify(this.host));
		sb.append("xbdd/rest/reports/");
		sb.append(slashify(this.projectKey));
		sb.append(slashify(this.projectVersion));
		sb.append(slashify(this.buildNumber));
		return sb.toString();
	}
	
	/**
	 * Ensure the string ends with a /
	 * @param s the string
	 * @return the string with a /
	 */
	private String slashify(String s) {
		return StringUtils.appendIfMissing(s, "/");	
	}
	

}
