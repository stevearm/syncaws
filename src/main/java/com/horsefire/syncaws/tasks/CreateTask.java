package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(CreateTask.class);

	private final CommandLineArgs m_args;
	private final ConfigService m_configService;
	private final UuidGenerator m_generator;
	private final AwsClient m_client;
	private final UrlService m_urlService;
	private final FileDelegate m_fileDelegate;

	@Inject
	public CreateTask(CommandLineArgs args, ConfigService configService,
			UuidGenerator generator, AwsClient client, UrlService urlService,
			FileDelegate fileDelegate) {
		m_args = args;
		m_configService = configService;
		m_generator = generator;
		m_client = client;
		m_urlService = urlService;
		m_fileDelegate = fileDelegate;
	}

	public void validate() {
		if (m_args.getArgs().size() != 2) {
			throw new RuntimeException(
					"Task expects project name and directory");
		}
	}

	public void run() throws Exception {
		List<String> args = m_args.getArgs();
		String projectName = args.get(0);
		File dir = new File(args.get(1));
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
		Project project = new Project(projectName, m_generator.getId(),
				m_generator.getId(), path);
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
		LOG.info("Added project {} and uploaded index files", projectName);
	}
}
