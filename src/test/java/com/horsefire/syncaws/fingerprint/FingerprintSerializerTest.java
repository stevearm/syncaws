package com.horsefire.syncaws.fingerprint;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;

public class FingerprintSerializerTest extends TestCase {

	private FingerprintSerializer m_serializer;
	private Fingerprint m_samplePrint;

	@Override
	protected void setUp() throws Exception {
		m_serializer = new FingerprintSerializer();
		List<FileInfo> files = new ArrayList<FileInfo>();
		files.add(new FileInfo("mysite.html",
				"59c9a5690432939ec043b23e6d06b3fb", 200));
		files.add(new FileInfo("notes.txt", "f3763c7b84c3f56d623391842e9fe149",
				100));
		files.add(new FileInfo("pics/bender.jpg",
				"5f7f49d5b140754e5cad05b9181dc6f5", 522));
		files.add(new FileInfo("pics/hypnotoad.gif",
				"c56c88d8651ab8d5d8d42b188cea3e6a", 45));
		files.add(new FileInfo("pics/zoidberg.jpg",
				"0db38976d12e7e566c19dd3bf1a68c03", 5543));
		m_samplePrint = new Fingerprint(new DateTime(2011, 9, 12, 23, 30),
				files);
	}

	@Test
	public void testLoad() throws IOException {
		Fingerprint print = m_serializer
				.load(new FileReader(
						"src/test/resources/config/4647dcb7-eb88-489e-bec7-c4468df846d5.js"));
		assertEquals(m_samplePrint, print);
	}

	@Test
	public void testRoundTrip() {
		Fingerprint newPrint = m_serializer.load(new StringReader(m_serializer
				.save(m_samplePrint)));
		assertEquals(m_samplePrint, newPrint);
	}
}
