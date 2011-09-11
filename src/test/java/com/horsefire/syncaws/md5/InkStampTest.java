package com.horsefire.syncaws.md5;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.NumberConverter;

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
	}

	@Test
	public void testDifferentDirs() {
		Fingerprint fingerprintA = m_stamp.createNew(new File(
				"src/test/resources/typeA"));
		Fingerprint fingerprintB = m_stamp.createNew(new File(
				"src/test/resources/typeB"));
		Fingerprint fingerprintC = m_stamp.createNew(new File(
				"src/test/resources/typeC"));
		assertFalse(fingerprintA.equals(fingerprintB));
		assertFalse(fingerprintB.equals(fingerprintA));
		assertFalse(fingerprintA.equals(fingerprintC));
		assertFalse(fingerprintC.equals(fingerprintA));
		assertFalse(fingerprintB.equals(fingerprintC));
		assertFalse(fingerprintC.equals(fingerprintB));
	}
}
