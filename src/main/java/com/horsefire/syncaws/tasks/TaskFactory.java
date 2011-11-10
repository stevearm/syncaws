package com.horsefire.syncaws.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.horsefire.syncaws.CommandLineArgs;

public class TaskFactory {

	private final CommandLineArgs m_args;
	private final Map<String, Provider<? extends Task>> m_taskMapping = new HashMap<String, Provider<? extends Task>>();

	@Inject
	public TaskFactory(CommandLineArgs args,
			Provider<InitTask> initTaskProvider,
			Provider<ValidateTask> validateTaskProvider,
			Provider<ScanTask> scanTaskProvider,
			Provider<UploadTask> uploadTaskProvider) {
		m_args = args;
		m_taskMapping.put("init", initTaskProvider);
		m_taskMapping.put("validate", validateTaskProvider);
		m_taskMapping.put("scan", scanTaskProvider);
		m_taskMapping.put("upload", uploadTaskProvider);
	}

	public Task[] parseTasks() {
		List<String> taskNames = m_args.getTasks();
		if (taskNames.size() == 0) {
			StringBuilder message = new StringBuilder(
					"Must specify atleast one of the followa tasks: ");
			for (String task : m_taskMapping.keySet()) {
				message.append(task).append(", ");
			}
			message.setLength(message.length() - 2);
			throw new RuntimeException(message.toString());
		}
		final Task[] tasks = new Task[taskNames.size()];
		for (int i = 0; i < tasks.length; i++) {
			Provider<? extends Task> provider = m_taskMapping.get(taskNames
					.get(i));
			if (provider == null) {
				throw new RuntimeException("Unknown task: " + taskNames.get(i));
			}
			tasks[i] = provider.get();
			tasks[i].validate();
		}
		return tasks;
	}
}
