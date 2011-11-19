package com.horsefire.syncaws;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgsBuilder {

	private String m_configDir = null;
	private String m_project = null;
	private String m_dir = null;
	private boolean m_debug = false;
	private boolean m_dryRun = false;
	private final List<String> m_tasks = new ArrayList<String>();

	public CommandLineArgsBuilder configDir(String configDir) {
		m_configDir = configDir;
		return this;
	}

	public CommandLineArgsBuilder project(String project) {
		m_project = project;
		return this;
	}

	public CommandLineArgsBuilder dir(String dir) {
		m_dir = dir;
		return this;
	}

	public CommandLineArgsBuilder debug() {
		m_debug = true;
		return this;
	}

	public CommandLineArgsBuilder dryRun() {
		m_dryRun = true;
		return this;
	}

	public CommandLineArgsBuilder addTask(String task) {
		m_tasks.add(task);
		return this;
	}

	public CommandLineArgs build() {
		return new CommandLineArgs(m_configDir, m_project, m_debug, m_tasks,
				m_dir, m_dryRun);
	}
}
