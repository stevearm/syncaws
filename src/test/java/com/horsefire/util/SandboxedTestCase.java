package com.horsefire.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public abstract class SandboxedTestCase extends TestCase {

	protected String getSandboxPath() {
		return "target/tests/" + getClass().getName();
	}

	protected void createSandbox() throws IOException {
		File dir = new File(getSandboxPath());
		if (dir.exists()) {
			TestHelper.delete(dir);
		}
		dir.mkdirs();
	}

	protected void deleteSandbox() throws IOException {
		File dir = new File(getSandboxPath());
		if (dir.exists()) {
			TestHelper.delete(dir);
		}
	}

	@Override
	protected void setUp() throws Exception {
		createSandbox();
	}

	@Override
	protected void tearDown() throws Exception {
		deleteSandbox();
	}
}
