package com.horsefire.syncaws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;

public class ConfigService {

	public static final String FILENAME = "syncaws.js";

	private transient Config m_config = null;

	public boolean load(File m_dir) {
		try {
			Reader in = new FileReader(new File(m_dir, FILENAME));
			Gson gson = new Gson();
			Config config = gson.fromJson(in, Config.class);
			assertNotEmpty("accessKey", config.accessKey);
			assertNotEmpty("secretAccessKey", config.secretAccessKey);
			assertNotEmpty("bucket", config.bucket);
			assertNotEmpty("baseUrl", config.baseUrl);
			m_config = config;
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	private static void assertNotEmpty(String name, String string) {
		if (string == null || string.isEmpty()) {
			throw new RuntimeException("Config did not validate. " + name
					+ " was empty");
		}
	}

	public String getAccessKey() {
		return m_config.accessKey;
	}

	public String getSecretAccessKey() {
		return m_config.secretAccessKey;
	}

	public String getBucket() {
		return m_config.bucket;
	}

	public String getBaseUrl() {
		return m_config.baseUrl;
	}

	public Project[] getProjects() {
		return m_config.projects;
	}
}
