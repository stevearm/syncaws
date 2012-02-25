package com.horsefire.syncaws.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.aws.UrlService;

public class ListTask implements Task {

	private static final Logger LOG = LoggerFactory.getLogger(ListTask.class);

	private final CommandLineArgs m_args;
	private final ConfigService m_configService;
	private final UrlService m_urlService;

	@Inject
	public ListTask(CommandLineArgs args, ConfigService configService,
			UrlService urlService) {
		m_args = args;
		m_configService = configService;
		m_urlService = urlService;
	}

	public void validate() {
		if (!m_args.getArgs().isEmpty()) {
			throw new RuntimeException("Task does not have arguments");
		}
	}

	public void run() throws Exception {
		for (Project project : m_configService.getProjects()) {
			LOG.info("Project {} ({}) at {}", new Object[] {
					project.getName(),
					project.getBaseDir(),
					"https://s3.amazonaws.com/" + m_configService.getBucket()
							+ "/" + m_urlService.getHtmlFile(project) });
		}
	}
}
