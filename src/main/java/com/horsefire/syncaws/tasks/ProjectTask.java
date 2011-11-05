package com.horsefire.syncaws.tasks;

import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;

public abstract class ProjectTask implements Task {

	private final CommandLineArgs m_options;
	private final ConfigService m_configService;
	private Project m_selectedProject;

	public ProjectTask(CommandLineArgs options, ConfigService configService) {
		m_options = options;
		m_configService = configService;
	}

	protected CommandLineArgs getOptions() {
		return m_options;
	}

	protected ConfigService getConfig() {
		return m_configService;
	}

	protected Project getSelectedProject() {
		return m_selectedProject;
	}

	public void validate() {
		for (Project project : getConfig().getProjects()) {
			if (project.getName().equals(getOptions().getProject())) {
				m_selectedProject = project;
				return;
			}
		}
		throw new RuntimeException("No project found for: "
				+ m_options.getProject());
	}
}
