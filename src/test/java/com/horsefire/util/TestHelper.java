package com.horsefire.util;

import java.io.File;

public class TestHelper {

	public static String getTestSandbox(Class<?> clazz) {
		String path = "target/tests/" + clazz.getName();
		File dir = new File(path);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		return path;
	}
}
