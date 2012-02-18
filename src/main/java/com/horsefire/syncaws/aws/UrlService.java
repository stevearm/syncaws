package com.horsefire.syncaws.aws;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.backup.UploadedFile;

public class UrlService {

	private final ConfigService m_config;

	@Inject
	public UrlService(ConfigService config) {
		m_config = config;
	}

	private String getProjectBaseUrl(Project project) {
		return m_config.getBaseUrl() + '/' + project.getId();
	}

	public String getIndexList(Project project) {
		return getProjectBaseUrl(project) + "/" + project.getIndexName()
				+ ".js";
	}

	public String getHtmlFile(Project project) {
		return getProjectBaseUrl(project) + "/" + project.getIndexName()
				+ ".html";
	}

	public String getIndex(Project project, String index) {
		return getProjectBaseUrl(project) + "/" + project.getIndexName()
				+ index;
	}

	public String getFile(Project project, UploadedFile file) {
		return getProjectBaseUrl(project) + "/files/" + file.getId();
	}

}
