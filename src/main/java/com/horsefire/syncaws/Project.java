package com.horsefire.syncaws;

import java.io.File;

import com.google.gson.annotations.SerializedName;
import com.horsefire.syncaws.SyncAws.Options;

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

	public File getFingerprintFile(Options options) {
		return new File(options.configDir, getId() + ".js");
	}

	// public String getNewestIndex
}