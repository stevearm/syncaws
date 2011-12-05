package com.horsefire.syncaws;

import java.io.File;

import com.google.gson.annotations.SerializedName;

public class Project {

	@SerializedName("name")
	private String m_name;

	@SerializedName("id")
	private String m_id;

	@SerializedName("baseDir")
	private String m_baseDir;

	public Project() {
		// Do nothing
	}

	public Project(String name, String id, String baseDir) {
		m_name = name;
		m_id = id;
		m_baseDir = baseDir;
	}

	public String getName() {
		return m_name;
	}

	public String getId() {
		return m_id;
	}

	public String getBaseDir() {
		return m_baseDir;
	}

	@Override
	public String toString() {
		return m_name + "(" + m_id + ")";
	}

	public File getFingerprintFile(CommandLineArgs options) {
		return new File(options.getConfigDir(), getId() + ".js");
	}
}