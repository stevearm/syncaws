package com.horsefire.syncaws;

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

	public static Config load(Reader in) {
		Gson gson = new Gson();
		return gson.fromJson(in, Config.class);
	}
}
