package com.horsefire.syncaws.tasks;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.util.TestHelper;

public class InitTaskTest extends TestCase {

	@Test
	public void testTask() throws Exception {
		List<String> projects = Collections.emptyList();
		CommandLineArgs args = new CommandLineArgs(
				TestHelper.getTestSandbox(getClass()), null, false, projects);
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
}
