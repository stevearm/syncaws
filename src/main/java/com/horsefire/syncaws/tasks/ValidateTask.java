package com.horsefire.syncaws.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.aws.AwsClient;

public class ValidateTask implements Task {

	private static final Logger LOG = LoggerFactory
			.getLogger(ValidateTask.class);

	private final CommandLineArgs m_args;
	private final ConfigService m_configService;
	private final AwsClient m_awsClient;

	@Inject
	public ValidateTask(CommandLineArgs args, ConfigService configService,
			AwsClient awsClient) {
		m_args = args;
		m_configService = configService;
		m_awsClient = awsClient;
	}

	public void validate() {
		if (!m_args.getArgs().isEmpty()) {
			throw new RuntimeException("Task does not have arguments");
		}
	}

	public void run() throws Exception {
		m_configService.getBaseUrl();
		try {
			m_awsClient.testConnection();
		} catch (Exception e) {
			throw new RuntimeException("Could not connect to S3", e);
		}
		LOG.info("Config file and S3 settings validated");
	}
}
