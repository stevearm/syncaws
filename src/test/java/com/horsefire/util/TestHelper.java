package com.horsefire.util;

import java.io.File;
import java.io.IOException;

public class TestHelper {

	public static String getTestSandbox(Class<?> clazz) throws IOException {
		String path = "target/tests/" + clazz.getName();
		File dir = new File(path);
		if (dir.exists()) {
			delete(dir);
		}
		dir.mkdirs();
		return path;
	}

	public static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new IOException("Failed to delete file: " + f);
	}
}
