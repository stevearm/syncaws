package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.util.TestHelper;

public class InitTaskTest extends TestCase {

	private String m_sandbox;

	@Override
	protected void setUp() throws Exception {
		m_sandbox = TestHelper.getTestSandbox(getClass());
	}

	private void deleteSandbox() throws IOException {
		File dir = new File(m_sandbox);
		if (dir.exists()) {
			TestHelper.delete(dir);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		deleteSandbox();
	}

	private void runInit() throws Exception {
		List<String> projects = Collections.emptyList();
		CommandLineArgs args = new CommandLineArgs(m_sandbox, null, false,
				projects);
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
