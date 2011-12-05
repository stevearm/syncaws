package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.Reader;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.UuidGenerator;
import com.horsefire.syncaws.aws.AwsClient;
import com.horsefire.syncaws.aws.UrlService;
import com.horsefire.syncaws.resources.Loader;
import com.horsefire.util.FileDelegate;

public class CreateTask implements Task {

	private final CommandLineArgs m_options;
	private final ConfigService m_configService;
	private final UuidGenerator m_generator;
	private final AwsClient m_client;
	private final UrlService m_urlService;
	private final FileDelegate m_fileDelegate;

	@Inject
	public CreateTask(CommandLineArgs options, ConfigService configService,
			UuidGenerator generator, AwsClient client, UrlService urlService,
			FileDelegate fileDelegate) {
		m_options = options;
		m_configService = configService;
		m_generator = generator;
		m_client = client;
		m_urlService = urlService;
		m_fileDelegate = fileDelegate;
	}

	public void validate() {
		String projectName = m_options.getProject();
		if (projectName == null || projectName.isEmpty()) {
			throw new RuntimeException(
					"Must specify project name when creating a project");
		}
		String dir = m_options.getDir();
		if (dir == null || dir.isEmpty()) {
			throw new RuntimeException(
					"Must specify dir when creating a project");
		}
	}

	public void run() throws Exception {
		String projectName = m_options.getProject();
		File dir = new File(m_options.getDir());
		if (!m_fileDelegate.isDirectory(dir)) {
			throw new RuntimeException("Directory must exist");
		}
		for (Project project : m_configService.getProjects()) {
			if (projectName.equals(project.getName())) {
				throw new RuntimeException("That project name is already taken");
			}
		}

		String path = m_fileDelegate.getAbsolutePath(dir);
		if (path.contains("\\")) {
			path = path.replaceAll("\\\\", "/");
		}
		Project project = new Project(projectName, m_generator.getId(), path);
		Reader reader = new Loader().getHtmlIndexFile();
		char[] buffer = new char[1000];
		int length = reader.read(buffer);
		StringBuilder htmlFile = new StringBuilder();
		while (length != -1) {
			htmlFile.append(buffer, 0, length);
			length = reader.read(buffer);
		}
		reader.close();
		m_client.putHtml(htmlFile.toString(), m_urlService.getHtmlFile(project));
		m_client.putJson("[]", m_urlService.getIndexList(project));
		m_configService.addProject(project);
	}
}
