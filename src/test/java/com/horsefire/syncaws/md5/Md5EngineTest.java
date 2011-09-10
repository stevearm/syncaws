package com.horsefire.syncaws.md5;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.util.NumberConverter;

public class Md5EngineTest extends TestCase {

	public static String getFileName(String fullPath) {
		String separator = File.separator;
		if (File.separatorChar == '\\') {
			separator = '\\' + separator;
		}
		String[] split = fullPath.split(separator);
		String[] split2 = split[split.length - 1].split("\\.");
		return split2[0];
	}

	@Test
	public void testGetFileName() {
		String hash = "235636583246245adcc4545b2";
		assertEquals(hash, getFileName("/mnt/full/path" + File.separator + hash
				+ ".txt"));
		assertEquals(hash, getFileName("C:\\mnt\\full\\path" + File.separator
				+ hash + ".txt"));
	}

	@Test
	public void testFileList() {
		File dir = new File("src/test/resources");
		Md5Engine engine = new Md5Engine(new Md5Calculator(
				new NumberConverter()));
		for (Md5File file : engine.walkDir(dir)) {
			assertEquals(file.getHash(), getFileName(file.getFile())
					.toUpperCase());
		}
	}
}
