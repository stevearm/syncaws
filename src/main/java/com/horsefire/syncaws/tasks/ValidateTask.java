package com.horsefire.syncaws.tasks;

import com.google.inject.Inject;
import com.horsefire.syncaws.aws.AwsClient;

public class ValidateTask implements Task {

	private final AwsClient m_awsClient;

	@Inject
	public ValidateTask(AwsClient awsClient) {
		m_awsClient = awsClient;
	}

	public void validate() {
		// Do nothing
	}

	public void run() throws Exception {
		try {
			m_awsClient.testConnection();
		} catch (Exception e) {
			throw new RuntimeException("Could not load config", e);
		}
	}
}
