package com.horsefire.syncaws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.horsefire.syncaws.tasks.Task;
import com.horsefire.syncaws.tasks.TaskFactory;

public class Driver {

	private static final Logger LOG = LoggerFactory.getLogger(SyncAws.class);

	public static void start(CommandLineArgs args) throws Exception {
		Injector injector = Guice.createInjector(new SyncModule(args));
		Driver main = injector.getInstance(Driver.class);
		main.run();
	}

	private final CommandLineArgs m_args;
	private final TaskFactory m_taskFactory;

	@Inject
	public Driver(CommandLineArgs args, TaskFactory taskFactory) {
		m_args = args;
		m_taskFactory = taskFactory;
	}

	public void run() throws Exception {
		if (m_args == null || m_args.getConfigDir() == null
				|| m_args.getConfigDir().isEmpty()) {
			LOG.error("Must specify a config directory");
			return;
		}

		Task[] tasks = null;
		try {
			tasks = m_taskFactory.parseTasks();
		} catch (RuntimeException e) {
			LOG.error("Error parsing tasks: {}", e.getMessage(), e);
			return;
		}
		try {
			for (Task task : tasks) {
				task.run();
			}
		} catch (RuntimeException e) {
			LOG.error("Error running task: {}", e.getMessage(), e);
		}
	}
}
