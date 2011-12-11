package com.horsefire.syncaws.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.aws.AwsClient;

public class ValidateTask implements Task {

	private static final Logger LOG = LoggerFactory
			.getLogger(ValidateTask.class);

	private final ConfigService m_configService;
	private final AwsClient m_awsClient;

	@Inject
	public ValidateTask(ConfigService configService, AwsClient awsClient) {
		m_configService = configService;
		m_awsClient = awsClient;
	}

	public void validate() {
		// Do nothing
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
