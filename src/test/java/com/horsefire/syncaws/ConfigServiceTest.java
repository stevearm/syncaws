package com.horsefire.syncaws;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.horsefire.util.SandboxedTestCase;

public class ConfigServiceTest extends SandboxedTestCase {

	@Test
	public void testDeserialize() throws FileNotFoundException {
		ConfigService config = new ConfigService(new CommandLineArgsBuilder()
				.configDir("src/test/resources/config").build());
		assertEquals("asdf", config.getAccessKey());
		assertEquals("aoifjwoeif", config.getSecretAccessKey());
		assertEquals("stevearm", config.getBucket());
		assertEquals("/backups", config.getBaseUrl());
		assertEquals(1, config.getProjects().size());
		assertProject(config.getProjects().iterator().next(), "docs",
				"f373d524-b218-46bc-a2bf-2adb91926048",
				"f373d524-b218-46bc-a2bf-2adb91926049",
				"c:/Users/steve/Documents");
	}

	private void assertProject(Project project, String name, String id,
			String indexName, String baseDir) {
		assertEquals(name, project.getName());
		assertEquals(id, project.getId());
		assertEquals(indexName, project.getIndexName());
		assertEquals(baseDir, project.getBaseDir());
	}

	@Test
	public void testNoFile() throws IOException {
		ConfigService config = new ConfigService(new CommandLineArgsBuilder()
				.configDir(getSandboxPath()).build());
		try {
			config.getAccessKey();
			fail("Should have thrown an exception");
		} catch (RuntimeException e) {
			// Expected
		}
	}
}
