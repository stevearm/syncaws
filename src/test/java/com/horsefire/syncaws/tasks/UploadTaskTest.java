package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.horsefire.util.TestHelper;

public class UploadTaskTest extends TestCase {

	private String m_sandbox;

	@Override
	protected void setUp() throws Exception {
		m_sandbox = TestHelper.getTestSandbox(getClass());
	}

	private void deleteSandbox() throws IOException {
		File dir = new File(m_sandbox);
		if (dir.exists()) {
			TestHelper.delete(dir);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		deleteSandbox();
	}
}
