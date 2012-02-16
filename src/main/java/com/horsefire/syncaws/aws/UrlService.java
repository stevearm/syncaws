package com.horsefire.syncaws.aws;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.backup.UploadedFile;

public class UrlService {

	public static final String INDEX_LIST = "indexList.js";
	public static final String HTML_FILE = "index.html";

	private final ConfigService m_config;

	@Inject
	public UrlService(ConfigService config) {
		m_config = config;
	}

	private String getProjectBaseUrl(Project project) {
		return m_config.getBaseUrl() + '/' + project.getId();
	}

	public String getIndexList(Project project) {
		return getProjectBaseUrl(project) + "/" + INDEX_LIST;
	}

	public String getHtmlFile(Project project) {
		return getProjectBaseUrl(project) + "/" + HTML_FILE;
	}

	public String getIndex(Project project, String index) {
		return getProjectBaseUrl(project) + "/" + index;
	}

	public String getFile(Project project, UploadedFile file) {
		return getProjectBaseUrl(project) + "/files/" + file.getId() + "/"
				+ file.getPath();
	}

}
