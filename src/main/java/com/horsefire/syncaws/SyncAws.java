package com.horsefire.syncaws;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
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
	public static class Options {

		@Parameter(names = { "-c", "--configDir" }, description = "SyncAws config directory", required = true)
		public String configDir;

		@Parameter(names = { "-p", "--project" }, description = "Project name")
		public String project;

		@Parameter(names = { "--debug" }, description = "Enable debug logging")
		public boolean debug = false;

		@Parameter
		public List<String> tasks = Lists.newArrayList();
	}

	private static Logger initLogging(boolean debug, String filePath) {
		if (debug) {
			System.setProperty("log-level", "true");
		}
		System.setProperty("log-path", filePath);
		Logger log = org.slf4j.LoggerFactory.getLogger(SyncAws.class);
		log.debug("Debug logging enabled");
		return log;
	}

	public static void main(String[] args) throws Exception {
		Options options = new Options();
		try {
			new JCommander(options, args);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			return;
		}

		Logger logger = initLogging(options.debug, options.configDir + '/');

		String errorMessage = "Error loading config";
		try {
			Config config = Config.load(new File(options.configDir));

			errorMessage = "Error parsing tasks";
			Task[] tasks = new TaskFactory().parseTasks(options, config);

			errorMessage = "Error running task";
			for (Task task : tasks) {
				task.run();
			}
		} catch (RuntimeException e) {
			logger.error(errorMessage, e);
		}
	}
}