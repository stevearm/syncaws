package com.horsefire.syncaws.tasks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.jets3t.service.S3ServiceException;
import org.junit.Test;

import com.horsefire.syncaws.aws.AwsClient;

public class ValidateTaskTest extends TestCase {

	@Test
	public void testHappyPath() throws Exception {
		AwsClient awsClient = createMock(AwsClient.class);
		awsClient.testConnection();
		expectLastCall().once();
		replay(awsClient);
		ValidateTask task = new ValidateTask(awsClient);
		task.validate();
		task.run();
		verify(awsClient);
	}

	@Test
	public void testRuntimeFailure() throws Exception {
		AwsClient awsClient = createMock(AwsClient.class);
		awsClient.testConnection();
		expectLastCall().andThrow(new RuntimeException()).once();
		replay(awsClient);
		ValidateTask task = new ValidateTask(awsClient);
		task.validate();
		try {
			task.run();
			fail("Should have thrown an exception");
		} catch (RuntimeException e) {
			// Expected
		}
		verify(awsClient);
	}

	@Test
	public void testS3Failure() throws Exception {
		AwsClient awsClient = createMock(AwsClient.class);
		awsClient.testConnection();
		expectLastCall().andThrow(new S3ServiceException()).once();
		replay(awsClient);
		ValidateTask task = new ValidateTask(awsClient);
		task.validate();
		try {
			task.run();
			fail("Should have thrown an exception");
		} catch (RuntimeException e) {
			// Expected
		}
		verify(awsClient);
	}
}
