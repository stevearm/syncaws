package com.horsefire.syncaws;

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

/**
 * 
 * @author steve
 * 
 */
public final class SyncAws {

	// syncaws -c configDir -p docs scan (rescan the docs project)
	// syncaws -c configDir -p docs diff (show what needs to be uploaded)
	// syncaws -c configDir -p docs upload (upload what needs to be)
	private static class Options {

		@Parameter(names = { "-c", "--configDir" }, description = "SyncAws config directory")
		public String configDir;

		@Parameter(names = { "-p", "--project" }, description = "Project name")
		public String project;

		@Parameter
		public List<String> parameters = Lists.newArrayList();
	}

	public static void main(String[] args) {
		Options options = new Options();
		new JCommander(options, args);
	}
}