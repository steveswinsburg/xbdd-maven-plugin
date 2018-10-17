
package au.com.flyingkite.xbdd;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.shared.utils.ReaderFactory;

/**
 * Stub project that pulls data from the pom it is defined in
 */
public class XbddProjectStub extends MavenProjectStub {

	public XbddProjectStub() {

		// read the
		final MavenXpp3Reader pomReader = new MavenXpp3Reader();
		Model model;
		try {
			model = pomReader.read(ReaderFactory.newXmlReader(new File(getBasedir(), "test-pom-minimal.xml")));
			setModel(model);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		setGroupId(model.getGroupId());
		setArtifactId(model.getArtifactId());
		setVersion(model.getVersion());

	}

	@Override
	public File getBasedir() {
		return new File(super.getBasedir() + "/src/test/resources/");
	}
}
