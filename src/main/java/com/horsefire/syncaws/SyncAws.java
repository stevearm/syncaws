package com.horsefire.syncaws;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;

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

	private static void initLogging(boolean debug) {
		final PrintStream out = System.out;
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				// Do nothing
			}
		}));
		if (debug) {
			System.setProperty("logging-detail-level", "true");
		}
		Logger log = org.slf4j.LoggerFactory.getLogger(SyncAws.class);
		System.setOut(out);
		log.debug("Debug logging enabled");
	}

	public static void main(String[] args) throws Exception {
		Options options = new Options();
		new JCommander(options, args);

		initLogging(options.debug);

		Config config = Config.load(new File(options.configDir));
		Task[] tasks = new TaskFactory().parseTasks(options, config);
		for (Task task : tasks) {
			task.run();
		}

	}
}