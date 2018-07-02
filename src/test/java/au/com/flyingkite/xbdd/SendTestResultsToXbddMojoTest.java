package au.com.flyingkite.xbdd;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class SendTestResultsToXbddMojoTest extends AbstractMojoTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPomExists() {
		final File pom = getTestPom();
		assertNotNull(pom);
		assertTrue(pom.exists());

	}

	public void testUploadGoalDefined() throws Exception {
		final File pom = getTestPom();

		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);
		assertNotNull(mojo);
	}

	public void testConfigurationInPluginXmlIsLoaded() {
		// test that the config is loaded in from the plugin xml
	}

	public void testConfigurationViaCommandLineIsLoaded() {
		// test that the config is loaded in from the commandline

	}

	private File getTestPom() {
		return getTestFile("src/test/resources/test-pom.xml");
	}

}
