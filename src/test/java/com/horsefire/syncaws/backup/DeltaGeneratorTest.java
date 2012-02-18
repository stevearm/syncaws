package com.horsefire.syncaws.backup;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.UuidGenerator;
import com.horsefire.syncaws.fingerprint.FileInfo;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;

public class DeltaGeneratorTest extends TestCase {

	@Test
	public void testNoOldIndex() throws FileNotFoundException {
		Fingerprint print = new FingerprintSerializer().load(new FileReader(
				new File("src/test/resources/config/typeCindex.js")));
		Delta delta = new DeltaGenerator(new UuidGenerator()).create(null,
				print);
		List<FileInfo> localFiles = print.getFiles();
		List<UploadedFile> remoteFiles = delta.getFilesToUpload();
		assertEquals(localFiles.size(), remoteFiles.size());
		for (int i = 0; i < localFiles.size(); i++) {
			FileInfo localFile = localFiles.get(i);
			assertTrue(remoteFiles.get(i).isSameFile(localFile));
		}
		assertEquals(265721, delta.getTotalBytes());
	}

	@Test
	public void testDiffFromOldIndex() throws FileNotFoundException {
		Fingerprint print = new FingerprintSerializer().load(new FileReader(
				new File("src/test/resources/config/typeCindex.js")));
		Index oldIndex = new IndexSerializer().load(new FileReader(new File(
				"src/test/resources/indexes/201109122330.js")));
		Delta delta = new DeltaGenerator(new UuidGenerator()).create(oldIndex,
				print);
		List<UploadedFile> remoteFiles = delta.getFilesToUpload();
		assertEquals(2, remoteFiles.size());
		assertTrue(remoteFiles.get(0).isSameFile(
				new FileInfo("notes.txt", "276E9B83326B36FDB45307EA58B657A6",
						598)));
		assertTrue(remoteFiles.get(1).isSameFile(
				new FileInfo("pics/fry.jpg",
						"449BC213E212B24EC8C544CA6B748D92", 27481)));
		assertEquals(598 + 27481, delta.getTotalBytes());
	}

	@Test
	public void testIdsInUploadedFiles() throws Exception {
		String guidString = "thisisansifdnalsdkjlsfa";
		UuidGenerator generator = createMock(UuidGenerator.class);
		expect(generator.getId()).andReturn(guidString).anyTimes();
		replay(generator);

		Fingerprint print = new FingerprintSerializer().load(new FileReader(
				new File("src/test/resources/config/typeCindex.js")));
		Delta delta = new DeltaGenerator(generator).create(null, print);
		List<UploadedFile> remoteFiles = delta.getFilesToUpload();

		UploadedFile file = remoteFiles.get(0);
		assertEquals("Got the wrong file", "mysite.html", file.getPath());
		assertEquals(guidString + "/mysite.html", file.getId());

		file = remoteFiles.get(2);
		assertEquals("Got the wrong file", "pics/bender.jpg", file.getPath());
		assertEquals(guidString + "/bender.jpg", file.getId());
	}
}
