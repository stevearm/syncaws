package com.horsefire.syncaws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

public class TaskFactory {

	static interface TaskCreator {
		Task create(CommandLineArgs options, ConfigService config);
	}

	private final Map<String, TaskCreator> m_taskMapping = new HashMap<String, TaskFactory.TaskCreator>();

	@Inject
	public TaskFactory() {
		m_taskMapping.put("scan", new TaskCreator() {
			public Task create(CommandLineArgs options, ConfigService config) {
				return new ScanTask(options, config);
			}
		});
		m_taskMapping.put("delta", new TaskCreator() {
			public Task create(CommandLineArgs options, ConfigService config) {
				return new DeltaTask(options, config);
			}
		});
	}

	public Task[] parseTasks(CommandLineArgs options, ConfigService config) {
		List<String> taskNames = options.getTasks();
		if (taskNames.size() == 0) {
			throw new RuntimeException("Must specify a task");
		}
		final Task[] tasks = new Task[taskNames.size()];
		for (int i = 0; i < tasks.length; i++) {
			TaskCreator creator = m_taskMapping.get(taskNames.get(i));
			if (creator == null) {
				throw new RuntimeException("Unknown task: " + taskNames.get(i));
			}
			tasks[i] = creator.create(options, config);
		}
		return tasks;
	}
}
