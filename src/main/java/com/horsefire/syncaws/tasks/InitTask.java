package com.horsefire.syncaws.tasks;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;

public class InitTask implements Task {

	private final CommandLineArgs m_args;
	private final ConfigService m_configService;

	@Inject
	public InitTask(CommandLineArgs args, ConfigService configService) {
		m_args = args;
		m_configService = configService;
	}

	public void validate() {
		if (!m_args.getArgs().isEmpty()) {
			throw new RuntimeException("Task does not have arguments");
		}
	}

	public void run() throws Exception {
		m_configService.resetConfig();
	}
}
