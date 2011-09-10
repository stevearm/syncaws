package com.horsefire.syncaws.md5;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.util.NumberConverter;

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
}
