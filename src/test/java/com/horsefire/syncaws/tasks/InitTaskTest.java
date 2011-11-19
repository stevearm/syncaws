package com.horsefire.syncaws.tasks;

import java.io.IOException;

import org.junit.Test;

import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.CommandLineArgsBuilder;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.util.SandboxedTestCase;

public class InitTaskTest extends SandboxedTestCase {

	private void runInit() throws Exception {
		CommandLineArgs args = new CommandLineArgsBuilder().configDir(
				getSandboxPath()).build();
		ConfigService configService = new ConfigService(args);
		InitTask task = new InitTask(configService);
		task.validate();
		task.run();

		assertFalse(configService.getAccessKey().isEmpty());
		assertFalse(configService.getSecretAccessKey().isEmpty());
		assertFalse(configService.getBucket().isEmpty());
		assertFalse(configService.getBaseUrl().isEmpty());
		assertTrue(configService.getProjects().isEmpty());
	}

	@Test
	public void testEmptyLocation() throws Exception {
		runInit();
	}

	@Test
	public void testExistingLocation() throws Exception {
		runInit();
		runInit();
	}

	@Test
	public void testInvalidLocation() throws IOException {
		deleteSandbox();
		try {
			runInit();
			fail("Should have failed");
		} catch (Exception e) {
			// Success
		}
	}
}
