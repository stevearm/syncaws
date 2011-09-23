package com.horsefire.syncaws;

import com.horsefire.syncaws.SyncAws.Options;

public class TaskFactory {

	public Task[] parseTasks(Options options, Config config) {
		if (options.tasks.size() == 0) {
			throw new RuntimeException("Must specify a task");
		}
		final Task[] tasks = new Task[options.tasks.size()];
		for (int i = 0; i < tasks.length; i++) {
			String taskName = options.tasks.get(i);
			if ("scan".equals(taskName)) {
				tasks[i] = new ScanTask(options, config);
			} else {
				throw new RuntimeException("Unknown task: " + taskName);
			}
		}
		return tasks;
	}
}
