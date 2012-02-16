package com.horsefire.syncaws;

import java.io.InputStream;
import java.util.Properties;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * This is the command-line start class. It's stripped down because it must
 * parse the command-line, then setup logging parameters according to the
 * command-line arguments, and only then (once logging can start) do we hand off
 * execution to Driver, which starts Guice and spin up everything
 */
public final class SyncAws {

	public static void main(String[] args) throws Exception {
		CommandLineArgs options = new CommandLineArgs();
		try {
			new JCommander(options, args);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			return;
		}

		// Setup environment for logging
		System.setProperty("log-path", options.getConfigDir() + '/');
		if (options.isDebug()) {
			System.setProperty("log-level", "DEBUG");
		}

		if (options.getTasks().size() == 1
				&& options.getTasks().get(0).equals("version")) {
			String path = "/META-INF/maven/com.horsefire/syncaws/pom.properties";
			InputStream stream = SyncAws.class.getResourceAsStream(path);
			Properties props = new Properties();
			props.load(stream);
			System.out.println("SyncAws " + props.get("version"));
			return;
		}

		Driver.start(options);
	}
}