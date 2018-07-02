/*
 * Copyright (c) Orchestral Developments Ltd and the Orion Health group of companies (2001 - 2018).
 *
 * This document is copyright. Except for the purpose of fair reviewing, no part
 * of this publication may be reproduced or transmitted in any form or by any
 * means, electronic or mechanical, including photocopying, recording, or any
 * information storage and retrieval system, without permission in writing from
 * the publisher. Infringers of copyright render themselves liable for
 * prosecution.
 */
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
