package com.horsefire.syncaws;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class CommandLineArgs {

	@Parameter(names = { "-c", "--configDir" }, description = "SyncAws config directory")
	private String configDir;

	@Parameter(names = { "--debug" }, description = "Enable debug logging")
	private boolean debug = false;

	@Parameter
	private List<String> args = Lists.newArrayList();

	public CommandLineArgs() {
	}

	CommandLineArgs(String configDir, boolean debug, List<String> args) {
		this.configDir = configDir;
		this.debug = debug;
		this.args = args;
	}

	public String getConfigDir() {
		return configDir;
	}

	public boolean isDebug() {
		return debug;
	}

	public String getTask() {
		if (args.size() > 0) {
			return args.get(0);
		}
		return null;
	}

	public List<String> getArgs() {
		if (args.size() > 1) {
			return Collections.unmodifiableList(args.subList(1, args.size()));
		}
		return Collections.emptyList();
	}
}
