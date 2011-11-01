package com.horsefire.syncaws;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Driver {

	private static final Logger LOG = LoggerFactory.getLogger(SyncAws.class);

	public static void start(CommandLineArgs args) throws Exception {
		Injector injector = Guice.createInjector(new SyncModule(args));
		Driver main = injector.getInstance(Driver.class);
		main.run(args);
	}

	private final TaskFactory m_taskFactory;
	private final ConfigService m_configService;

	@Inject
	public Driver(TaskFactory taskFactory, ConfigService configService) {
		m_taskFactory = taskFactory;
		m_configService = configService;
	}

	public void run(CommandLineArgs options) throws Exception {
		try {
			m_configService.load(new File(options.getConfigDir()));
		} catch (RuntimeException e) {
			LOG.error("Error loading config", e);
			return;
		}

		Task[] tasks = null;
		try {
			tasks = m_taskFactory.parseTasks(options, m_configService);
		} catch (RuntimeException e) {
			LOG.error("Error parsing tasks", e);
			return;
		}
		try {
			for (Task task : tasks) {
				task.run();
			}
		} catch (RuntimeException e) {
			LOG.error("Error running task", e);
		}
	}
}
