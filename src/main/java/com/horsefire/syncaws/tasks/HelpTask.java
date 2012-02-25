package com.horsefire.syncaws.tasks;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;

public class HelpTask implements Task {

	private final CommandLineArgs m_args;

	@Inject
	public HelpTask(CommandLineArgs args) {
		m_args = args;
	}

	public void validate() {
		if (!m_args.getArgs().isEmpty()) {
			throw new RuntimeException("Task does not have arguments");
		}
	}

	public void run() throws Exception {
		throw new UnsupportedOperationException("Help not implemented");
	}
}
