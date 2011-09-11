package com.horsefire.syncaws.md5;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class FingerprintTest extends TestCase {

	private static final Md5File FILE_1 = new Md5File("fileA", "AD434EA");
	private static final Md5File FILE_2 = new Md5File("fileB", "AD434EA");
	private static final Md5File FILE_3 = new Md5File("folder/fileC", "AD434EA");

	private Fingerprint create(Md5File... files) {
		List<Md5File> fileList = new ArrayList<Md5File>();
		for (Md5File file : files) {
			fileList.add(file);
		}
		return new Fingerprint(fileList);
	}

	public void testEqualsOutOfOrder() {
		Fingerprint print1 = create(FILE_1, FILE_2, FILE_3);
		Fingerprint print2 = create(FILE_3, FILE_1, FILE_2);
		assertTrue("Out of order md5s should still be equal",
				print1.equals(print2));
	}

	public void testEqualsDifferent() {
		Fingerprint print1 = create(FILE_1, FILE_2, FILE_3);
		Fingerprint print2 = create(FILE_3);
		assertFalse("Fingerprints shouldn't be equals", print1.equals(print2));
	}
}
