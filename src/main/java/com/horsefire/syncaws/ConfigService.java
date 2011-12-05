package com.horsefire.syncaws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigService {

	public static final String FILENAME = "syncaws.js";
	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigService.class);

	private transient Config m_config = null;

	private final CommandLineArgs m_args;

	@Inject
	public ConfigService(CommandLineArgs args) {
		m_args = args;
	}

	private File getFile() {
		return new File(m_args.getConfigDir(), FILENAME);
	}

	private Config getConfig() {
		if (m_config == null) {
			try {
				Reader in = new FileReader(getFile());
				Config config = new Gson().fromJson(in, Config.class);
				assertNotEmpty("accessKey", config.accessKey);
				assertNotEmpty("secretAccessKey", config.secretAccessKey);
				assertNotEmpty("bucket", config.bucket);
				assertNotEmpty("baseUrl", config.baseUrl);
				m_config = config;
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error loading config from disk", e);
			}
		}
		return m_config;
	}

	private static void assertNotEmpty(String name, String string) {
		if (string == null || string.isEmpty()) {
			throw new RuntimeException("Config did not validate. " + name
					+ " was empty");
		}
	}

	public String getAccessKey() {
		return getConfig().accessKey;
	}

	public String getSecretAccessKey() {
		return getConfig().secretAccessKey;
	}

	public String getBucket() {
		return getConfig().bucket;
	}

	public String getBaseUrl() {
		return getConfig().baseUrl;
	}

	public Collection<Project> getProjects() {
		Project[] projects = getConfig().projects;
		if (projects == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(Arrays.asList(projects));
	}

	public void addProject(Project project) {
		LOG.debug("Adding new project: {}", project);
		Project[] projects = new Project[getConfig().projects.length + 1];
		System.arraycopy(getConfig().projects, 0, projects, 0,
				getConfig().projects.length);
		projects[getConfig().projects.length] = project;
		saveConfig(new Config(m_config, projects));
	}

	public void resetConfig() {
		LOG.debug("Resetting config to default");
		saveConfig(new Config());
	}

	private void saveConfig(Config config) {
		if (LOG.isDebugEnabled()) {
			String json = new Gson().toJson(m_config);
			LOG.debug("About to overwrite current config: " + json);
		}
		try {
			Writer writer = new FileWriter(getFile());
			writer.append(new GsonBuilder().setPrettyPrinting().create()
					.toJson(config));
			writer.close();
			m_config = config;
			if (LOG.isDebugEnabled()) {
				String json = new Gson().toJson(m_config);
				LOG.debug("Saved new config to disk: " + json);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error saving config to disk", e);
		}
	}

}
