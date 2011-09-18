package com.horsefire.syncaws.fingerprint;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import com.horsefire.syncaws.fingerprint.Fingerprint.Diff;

public class FingerprintTest extends TestCase {

	public static final FileInfo FILE_1 = new FileInfo("fileA", "AD434EA4");
	public static final FileInfo FILE_2 = new FileInfo("fileB", "AD475329");
	private static final String FILENAME_3 = "folder/fileC";
	private static final FileInfo FILE_3 = new FileInfo(FILENAME_3, "2435FF3A");

	public static Fingerprint create(FileInfo... files) {
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		for (FileInfo file : files) {
			fileList.add(file);
		}
		return new Fingerprint(new DateTime(), fileList);
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

	public void testDiff() {
		Fingerprint baseFingerprint = create(FILE_1, FILE_2, FILE_3);
		Fingerprint baseFingerprint2 = create(FILE_1, FILE_2, FILE_3);
		Diff diff = baseFingerprint.diff(baseFingerprint2);
		assertTrue(diff.addedFiles().isEmpty());
		assertTrue(diff.removedFiles().isEmpty());

		FileInfo md5File = new FileInfo("folder/fileD", "EE321A8");
		Fingerprint addedFingerprint = create(FILE_1, FILE_2, FILE_3, md5File);
		diff = baseFingerprint.diff(addedFingerprint);
		assertEquals(1, diff.addedFiles().size());
		assertEquals(md5File, diff.addedFiles().get(0));
		assertTrue(diff.removedFiles().isEmpty());

		Fingerprint removedFingerprint = create(FILE_1, FILE_2);
		diff = baseFingerprint.diff(removedFingerprint);
		assertTrue(diff.addedFiles().isEmpty());
		assertEquals(1, diff.removedFiles().size());
		assertEquals(FILE_3, diff.removedFiles().get(0));

		Fingerprint changedFingerprint = create(FILE_1, FILE_2, md5File);
		diff = baseFingerprint.diff(changedFingerprint);
		assertEquals(1, diff.addedFiles().size());
		assertEquals(md5File, diff.addedFiles().get(0));
		assertEquals(1, diff.removedFiles().size());
		assertEquals(FILE_3, diff.removedFiles().get(0));
	}
}
