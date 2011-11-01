package com.horsefire.syncaws;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class CommandLineArgs {

	@Parameter(names = { "-c", "--configDir" }, description = "SyncAws config directory", required = true)
	private String configDir;

	@Parameter(names = { "-p", "--project" }, description = "Project name")
	private final String project = null;

	@Parameter(names = { "--debug" }, description = "Enable debug logging")
	private final boolean debug = false;

	@Parameter
	private final List<String> tasks = Lists.newArrayList();

	public String getConfigDir() {
		return configDir;
	}

	public String getProject() {
		return project;
	}

	public boolean isDebug() {
		return debug;
	}

	public List<String> getTasks() {
		return Collections.unmodifiableList(tasks);
	}
}
