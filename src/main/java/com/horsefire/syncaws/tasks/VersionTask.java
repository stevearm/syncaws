package com.horsefire.syncaws.tasks;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionTask implements Task {

	private static final Logger LOG = LoggerFactory
			.getLogger(VersionTask.class);

	public VersionTask() {
	}

	public void validate() {
		// Do nothing
	}

	public void run() throws Exception {
		String path = "/META-INF/maven/com.horsefire/syncaws/pom.properties";
		InputStream stream = getClass().getResourceAsStream(path);
		Properties props = new Properties();
		props.load(stream);
		LOG.info("SyncAws {}", props.get("version"));
	}
}
