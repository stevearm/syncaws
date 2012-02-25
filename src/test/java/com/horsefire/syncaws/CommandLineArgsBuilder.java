package com.horsefire.syncaws;

import java.util.ArrayList;
import java.util.List;

public class CommandLineArgsBuilder {

	private String m_configDir = null;
	private boolean m_debug = false;
	private final List<String> m_args = new ArrayList<String>();

	public CommandLineArgsBuilder configDir(String configDir) {
		m_configDir = configDir;
		return this;
	}

	public CommandLineArgsBuilder project(String project) {
		throw new UnsupportedOperationException();
	}

	public CommandLineArgsBuilder dir(String dir) {
		throw new UnsupportedOperationException();
	}

	public CommandLineArgsBuilder debug() {
		m_debug = true;
		return this;
	}

	public CommandLineArgsBuilder dryRun() {
		throw new UnsupportedOperationException();
	}

	public CommandLineArgsBuilder addArgs(String args) {
		m_args.add(args);
		return this;
	}

	public CommandLineArgs build() {
		return new CommandLineArgs(m_configDir, m_debug, m_args);
	}
}
