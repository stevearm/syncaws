package com.horsefire.syncaws.tasks;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;

public class InitTask implements Task {

	private final ConfigService m_configService;

	@Inject
	public InitTask(ConfigService configService) {
		m_configService = configService;
	}

	public void validate() {
		// Do nothing
	}

	public void run() throws Exception {
		m_configService.resetConfig();
	}
}
