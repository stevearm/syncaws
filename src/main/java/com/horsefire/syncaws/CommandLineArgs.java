package com.horsefire.syncaws;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class CommandLineArgs {

	@Parameter(names = { "-c", "--configDir" }, description = "SyncAws config directory", required = true)
	private String configDir;

	@Parameter(names = { "-p", "--project" }, description = "Project name")
	private String project = null;

	@Parameter(names = { "--dir" }, description = "Project directory")
	private String dir = null;

	@Parameter(names = { "--debug" }, description = "Enable debug logging")
	private boolean debug = false;

	@Parameter(names = { "--dry-run" }, description = "Dry run for upload")
	private boolean dryrun = false;

	@Parameter
	private List<String> tasks = Lists.newArrayList();

	public CommandLineArgs() {
	}

	CommandLineArgs(String configDir, String project, boolean debug,
			List<String> tasks, String dir, boolean dryRun) {
		this.configDir = configDir;
		this.project = project;
		this.debug = debug;
		this.tasks = tasks;
		this.dryrun = dryRun;
		this.dir = dir;
	}

	public String getConfigDir() {
		return configDir;
	}

	public String getProject() {
		return project;
	}

	public String getDir() {
		return dir;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isDryrun() {
		return dryrun;
	}

	public List<String> getTasks() {
		return Collections.unmodifiableList(tasks);
	}
}
