package com.horsefire.syncaws.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.horsefire.syncaws.CommandLineArgs;

public class TaskFactory {

	private final CommandLineArgs m_args;
	private final Map<String, Provider<? extends Task>> m_taskMapping = new HashMap<String, Provider<? extends Task>>();

	@Inject
	public TaskFactory(CommandLineArgs args,
			Provider<VersionTask> versionTaskProvider,
			Provider<InitTask> initTaskProvider,
			Provider<ValidateTask> validateTaskProvider,
			Provider<CreateTask> createTaskProvider,
			Provider<StatusTask> statusTaskProvider,
			Provider<UploadTask> uploadTaskProvider,
			Provider<HelpTask> helpTaskProvider,
			Provider<ListTask> listTaskProvider) {
		m_args = args;
		m_taskMapping.put("init", initTaskProvider);
		m_taskMapping.put("validate", validateTaskProvider);
		m_taskMapping.put("create", createTaskProvider);
		m_taskMapping.put("status", statusTaskProvider);
		m_taskMapping.put("upload", uploadTaskProvider);
		m_taskMapping.put("list", listTaskProvider);
		m_taskMapping.put("version", versionTaskProvider);
		m_taskMapping.put("help", helpTaskProvider);
	}

	public Task getTask() {
		String taskName = m_args.getTask();
		if (taskName == null) {
			throw new RuntimeException("Must specify a task: "
					+ StringUtils.join(m_taskMapping.keySet(), ", "));
		}
		Provider<? extends Task> provider = m_taskMapping.get(taskName);
		if (provider == null) {
			throw new RuntimeException("Unknown task: " + taskName);
		}
		Task task = provider.get();
		task.validate();
		return task;
	}
}
