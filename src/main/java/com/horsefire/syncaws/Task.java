package com.horsefire.syncaws;

import com.horsefire.syncaws.SyncAws.Options;

public abstract class Task {

	private final Options m_options;
	private final Config m_config;
	private Project m_selectedProject;

	public Task(Options options, Config config) {
		m_options = options;
		m_config = config;

		validate();
	}

	protected Options getOptions() {
		return m_options;
	}

	protected Config getConfig() {
		return m_config;
	}

	protected Project getSelectedProject() {
		return m_selectedProject;
	}

	protected void requireSelectedProject() {
		for (Project project : getConfig().getProjects()) {
			if (project.getName().equals(getOptions().project)) {
				m_selectedProject = project;
				return;
			}
		}
		throw new RuntimeException("No project found for: " + m_options.project);
	}

	public abstract void validate();

	public abstract void run() throws Exception;
}
