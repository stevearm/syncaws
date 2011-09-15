package com.horsefire.syncaws.backup;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;

import com.horsefire.syncaws.fingerprint.FileInfo;

public class IndexSerializerTest extends TestCase {

	private IndexSerializer m_serializer;
	private Index m_sampleIndex;

	@Override
	protected void setUp() throws Exception {
		m_serializer = new IndexSerializer();
		List<UploadedFile> files = new ArrayList<UploadedFile>();
		files.add(new UploadedFile(new FileInfo("mysite.html",
				"59c9a5690432939ec043b23e6d06b3fb", 22837),
				"c71f616f-2243-4726-aadc-e652b1ae5960"));
		files.add(new UploadedFile(new FileInfo("notes.txt",
				"f3763c7b84c3f56d623391842e9fe149", 583),
				"6fee6abb-bf20-4338-b19e-9b9c9f624f60"));
		files.add(new UploadedFile(new FileInfo("pics/bender.jpg",
				"5f7f49d5b140754e5cad05b9181dc6f5", 168697),
				"ea3a340e-2bde-43f6-aa79-9b809b0fffb4"));
		files.add(new UploadedFile(new FileInfo("pics/hypnotoad.gif",
				"c56c88d8651ab8d5d8d42b188cea3e6a", 46108),
				"da56b41c-b2bb-43fb-8d78-ec0ebbf8f1d9"));
		files.add(new UploadedFile(new FileInfo("pics/zoidberg.jpg",
				"0db38976d12e7e566c19dd3bf1a68c03", 28876),
				"163fdf5c-ed40-4817-872f-286b7747ee94"));
		m_sampleIndex = new Index(new DateTime(2011, 9, 12, 23, 30), null,
				files);
	}

	@Test
	public void testLoad() throws IOException {
		Index index = m_serializer.load(new FileReader(
				"src/test/resources/indexes/201109122330.js"));
		assertEquals(m_sampleIndex, index);
	}

	@Test
	public void testRoundTrip() {
		Index newIndex = m_serializer.load(new StringReader(m_serializer
				.save(m_sampleIndex)));
		assertEquals(m_sampleIndex, newIndex);
	}
}
