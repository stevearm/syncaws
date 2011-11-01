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

	public String getName() {
		return m_name;
	}

	public String getId() {
		return m_id;
	}

	public String getBaseDir() {
		return m_baseDir;
	}

	public File getFingerprintFile(CommandLineArgs options) {
		return new File(options.getConfigDir(), getId() + ".js");
	}
}