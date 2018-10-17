package au.com.flyingkite.xbdd;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.mockito.Mockito;

import lombok.Getter;

public class SendTestResultsToXbddMojoTest extends AbstractMojoTestCase {

	/**
	 * The various configs we test
	 */
	private enum PomType {
		BLANK("src/test/resources/test-pom-blank.xml"),
		MINIMAL("src/test/resources/test-pom-minimal.xml"),
		FULL("src/test/resources/test-pom-full.xml");

		@Getter
		private final String path;

		PomType(final String path) {
			this.path = path;
		}
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test_should_loadPom() {
		final File pom = getTestPom(PomType.MINIMAL);
		assertNotNull(pom);
		assertTrue(pom.exists());
	}

	public void test_should_defineGoal() throws Exception {
		final File pom = getTestPom(PomType.MINIMAL);

		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);
		assertNotNull(mojo);
	}

	public void test_should_loadConfig_and_includeDefaultsFromProject_when_minimallyConfigured() throws Exception {
		final File pom = getTestPom(PomType.MINIMAL);

		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);
		mojo.execute();

		assertEquals("Hostname did not match", "http://xbdd.io", mojo.getHost());
		assertEquals("Username did not match", "xbdd", mojo.getUsername());
		assertEquals("Password did not match", "password", mojo.getPassword());
		assertEquals("Artifact ID did not match", mojo.getProject().getArtifactId(), mojo.getProjectKey());
		assertEquals("Version did not match", "1.0.0", mojo.getProjectVersion());
		assertNotNull("Build number should not be null", mojo.getBuildNumber());
	}

	public void test_should_loadConfig_when_fullyConfigured() throws Exception {
		final File pom = getTestPom(PomType.FULL);

		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);
		mojo.execute();

		assertEquals("Hostname did not match", "http://xbdd.io", mojo.getHost());
		assertEquals("Username did not match", "xbdd", mojo.getUsername());
		assertEquals("Password did not match", "password", mojo.getPassword());
		assertEquals("Artifact ID did not match", "xbdd-maven-plugin-test", mojo.getProjectKey());
		assertEquals("Version did not match", "1.0.0", mojo.getProjectVersion());
		assertEquals("Build number did not match", "999", mojo.getBuildNumber());
	}

	public void test_should_notUpload_when_noBaseConfigurationDefined() throws Exception {

		final File pom = getTestPom(PomType.BLANK);
		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);

		final SendTestResultsToXbddMojo spy = Mockito.spy(mojo);
		spy.execute();

		Mockito.verify(spy, Mockito.never()).upload();
	}

	public void testConfigurationViaCommandLineIsLoaded() {
		// TODO test that the config is loaded in from the commandline

	}

	private File getTestPom(final PomType type) {
		return getTestFile(type.getPath());
	}

}
