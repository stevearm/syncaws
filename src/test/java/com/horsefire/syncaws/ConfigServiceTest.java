package com.horsefire.syncaws;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.util.TestHelper;

public class ConfigServiceTest extends TestCase {

	@Test
	public void testDeserialize() throws FileNotFoundException {
		ConfigService config = new ConfigService(new CommandLineArgs(
				"src/test/resources/config", null, false, null));
		assertEquals("asdf", config.getAccessKey());
		assertEquals("aoifjwoeif", config.getSecretAccessKey());
		assertEquals("stevearm", config.getBucket());
		assertEquals("/backups", config.getBaseUrl());
		assertEquals(1, config.getProjects().size());
		assertProject(config.getProjects().iterator().next(), "docs",
				"f373d524-b218-46bc-a2bf-2adb91926048",
				"c:/Users/steve/Documents");
	}

	private void assertProject(Project project, String name, String id,
			String baseDir) {
		assertEquals(name, project.getName());
		assertEquals(id, project.getId());
		assertEquals(baseDir, project.getBaseDir());
	}

	@Test
	public void testNoFile() throws IOException {
		String path = TestHelper.getTestSandbox(getClass());
		ConfigService config = new ConfigService(new CommandLineArgs(path,
				null, false, null));
		try {
			config.getAccessKey();
			fail("Should have thrown an exception");
		} catch (RuntimeException e) {
			// Expected
		}
	}
}
