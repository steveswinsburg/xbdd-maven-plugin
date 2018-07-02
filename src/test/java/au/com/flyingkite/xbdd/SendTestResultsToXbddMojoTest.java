package au.com.flyingkite.xbdd;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.mockito.Mock;

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

	@Mock
	SendTestResultsToXbddMojo mockMojo;

	@Mock
	Log mockLog;

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

		assertEquals("http://xbdd.io", mojo.getHost());
		assertEquals("xbdd", mojo.getUsername());
		assertEquals("password", mojo.getPassword());
		assertEquals(mojo.getProject().getArtifactId(), mojo.getProjectKey());
		assertEquals(mojo.getProject().getVersion(), mojo.getProjectVersion());
		assertNotNull(mojo.getBuildNumber());
	}

	public void test_should_loadConfig_when_fullyConfigured() throws Exception {
		final File pom = getTestPom(PomType.FULL);

		final SendTestResultsToXbddMojo mojo = (SendTestResultsToXbddMojo) lookupMojo("upload", pom);
		assertEquals("http://xbdd.io", mojo.getHost());
		assertEquals("xbdd", mojo.getUsername());
		assertEquals("password", mojo.getPassword());
		assertEquals("xbdd-maven-plugin-tester", mojo.getProjectKey());
		assertEquals("1.0", mojo.getProjectVersion());
		assertEquals("999", mojo.getBuildNumber());
	}

	public void test_should_failUpload_when_noBaseConfigurationDefined() throws Exception {
		// this.mockMojo = Mockito.mock(SendTestResultsToXbddMojo.class);
		// this.mockMojo.execute();
		// Mockito.verify(this.mockMojo, Mockito.never()).upload();
	}

	public void testConfigurationViaCommandLineIsLoaded() {
		// test that the config is loaded in from the commandline

	}

	private File getTestPom(final PomType type) {
		return getTestFile(type.getPath());
	}

}
