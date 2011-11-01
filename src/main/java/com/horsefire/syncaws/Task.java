package com.horsefire.syncaws;

public abstract class Task {

	private final CommandLineArgs m_options;
	private final ConfigService m_config;
	private Project m_selectedProject;

	public Task(CommandLineArgs options, ConfigService config) {
		m_options = options;
		m_config = config;

		validate();
	}

	protected CommandLineArgs getOptions() {
		return m_options;
	}

	protected ConfigService getConfig() {
		return m_config;
	}

	protected Project getSelectedProject() {
		return m_selectedProject;
	}

	protected void requireSelectedProject() {
		for (Project project : getConfig().getProjects()) {
			if (project.getName().equals(getOptions().getProject())) {
				m_selectedProject = project;
				return;
			}
		}
		throw new RuntimeException("No project found for: "
				+ m_options.getProject());
	}

	public abstract void validate();

	public abstract void run() throws Exception;
}
