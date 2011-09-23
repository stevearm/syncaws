package com.horsefire.syncaws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Config {

	public static final String FILENAME = "syncaws.js";

	@SerializedName("accessKey")
	private String m_accessKey;

	@SerializedName("secretAccessKey")
	private String m_secretAccessKey;

	@SerializedName("bucket")
	private String m_bucket;

	@SerializedName("baseUrl")
	private String m_baseUrl;

	@SerializedName("projects")
	private Project[] m_projects;

	public String getAccessKey() {
		return m_accessKey;
	}

	public String getSecretAccessKey() {
		return m_secretAccessKey;
	}

	public String getBucket() {
		return m_bucket;
	}

	public String getBaseUrl() {
		return m_baseUrl;
	}

	public Project[] getProjects() {
		return m_projects;
	}

	public static Config load(File dir) {
		try {
			Reader in = new FileReader(new File(dir, FILENAME));
			Gson gson = new Gson();
			Config config = gson.fromJson(in, Config.class);
			validate(config);
			return config;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("No " + FILENAME
					+ " file found in specified config dir");
		}
	}

	private static void validate(Config config) {
		assertNotEmpty("accessKey", config.m_accessKey);
		assertNotEmpty("secretAccessKey", config.m_secretAccessKey);
		assertNotEmpty("bucket", config.m_bucket);
		assertNotEmpty("baseUrl", config.m_baseUrl);
	}

	private static void assertNotEmpty(String name, String string) {
		if (string == null || string.isEmpty()) {
			throw new RuntimeException("Config did not validate. " + name
					+ " was empty");
		}
	}
}
