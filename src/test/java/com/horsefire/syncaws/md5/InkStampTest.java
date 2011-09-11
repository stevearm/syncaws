package com.horsefire.syncaws.md5;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.NumberConverter;
import com.horsefire.syncaws.md5.Fingerprint.Diff;

public class InkStampTest extends TestCase {

	private static final Md5File FILE_A = new Md5File("mysite.html",
			"59C9A5690432939EC043B23E6D06B3FB");

	private InkStamp m_stamp;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Md5Engine engine = new Md5Engine(new Md5Calculator(
				new NumberConverter()));
		m_stamp = new InkStamp(engine);
	}

	@Test
	public void testCreateNew() throws Exception {
		Fingerprint fingerprint = m_stamp.createNew(new File(
				"src/test/resources/typeA"));
		Md5File file = fingerprint.getFiles().iterator().next();
		assertEquals(FILE_A, file);
	}

	@Test
	public void testIdenticalDirs() {
		Fingerprint fingerprintA = m_stamp.createNew(new File(
				"src/test/resources/typeA"));
		Fingerprint fingerprintA2 = m_stamp.createNew(new File(
				"src/test/resources/typeA2"));
		assertTrue(fingerprintA.equals(fingerprintA2));
		assertTrue(fingerprintA2.equals(fingerprintA));
		Diff diff = fingerprintA.diff(fingerprintA2);
		assertTrue(diff.addedFiles().isEmpty());
		assertTrue(diff.removedFiles().isEmpty());
	}

	@Test
	public void testDifferentDirs() {
		Fingerprint fingerprintA = m_stamp.createNew(new File(
				"src/test/resources/typeA"));
		Fingerprint fingerprintB = m_stamp.createNew(new File(
				"src/test/resources/typeB"));
		assertFalse(fingerprintA.equals(fingerprintB));
		assertFalse(fingerprintB.equals(fingerprintA));
		Diff diff = fingerprintA.diff(fingerprintB);
		assertEquals(1, diff.addedFiles().size());
		assertEquals("notes.txt", diff.addedFiles().get(0).getFile());
		assertEquals(1, diff.removedFiles().size());
		assertEquals("notes.txt", diff.removedFiles().get(0).getFile());

		Fingerprint fingerprintC = m_stamp.createNew(new File(
				"src/test/resources/typeC"));
		assertFalse(fingerprintA.equals(fingerprintC));
		assertFalse(fingerprintC.equals(fingerprintA));
		diff = fingerprintA.diff(fingerprintC);
		assertEquals(1, diff.addedFiles().size());
		assertEquals("pics/fry.jpg", diff.addedFiles().get(0).getFile());
		assertEquals(1, diff.removedFiles().size());
		assertEquals("pics/zoidberg.jpg", diff.removedFiles().get(0).getFile());

		assertFalse(fingerprintB.equals(fingerprintC));
		assertFalse(fingerprintC.equals(fingerprintB));
		diff = fingerprintB.diff(fingerprintC);
		assertEquals(2, diff.addedFiles().size());
		assertEquals("notes.txt", diff.addedFiles().get(0).getFile());
		assertEquals("pics/fry.jpg", diff.addedFiles().get(1).getFile());
		assertEquals(2, diff.removedFiles().size());
		assertEquals("notes.txt", diff.removedFiles().get(0).getFile());
		assertEquals("pics/zoidberg.jpg", diff.removedFiles().get(1).getFile());
	}

	@Test
	public void testSaveFingerprint() {
	}
}
