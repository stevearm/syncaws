package com.horsefire.syncaws;

import java.io.FileNotFoundException;
import java.io.FileReader;

import junit.framework.TestCase;

import org.junit.Test;

public class ConfigTest extends TestCase {

	@Test
	public void testDeserialize() throws FileNotFoundException {
		Config config = Config.load(new FileReader("src/test/resources/config/"
				+ Config.FILENAME));
		assertEquals("asdf", config.getAccessKey());
		assertEquals("aoifjwoeif", config.getSecretAccessKey());
		assertEquals("stevearm", config.getBucket());
		assertEquals("/backups", config.getBaseUrl());
		assertEquals(1, config.getProjects().length);
		assertProject(config.getProjects()[0], "docs",
				"f373d524-b218-46bc-a2bf-2adb91926048",
				"c:/Users/steve/Documents");
	}

	private void assertProject(Project project, String name, String id,
			String baseDir) {
		assertEquals(name, project.getName());
		assertEquals(id, project.getId());
		assertEquals(baseDir, project.getBaseDir());
	}
}
