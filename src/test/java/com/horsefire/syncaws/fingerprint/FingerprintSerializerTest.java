package com.horsefire.syncaws.fingerprint;

import junit.framework.TestCase;

public class FingerprintSerializerTest extends TestCase {

	public void testToString() {
		Fingerprint print = FingerprintTest.create(FingerprintTest.FILE_1,
				FingerprintTest.FILE_2);
		String json = new FingerprintSerializer().toString(print);
		String[] lines = json.split("\\n");
		assertEquals(4, lines.length);
		assertEquals(lines[0], FingerprintSerializer.PAGE_START);
		assertEquals(
				lines[1],
				FingerprintSerializer.LINE_START
						+ FingerprintTest.FILE_1.getFile()
						+ FingerprintSerializer.LINE_MID
						+ FingerprintTest.FILE_1.getHash()
						+ FingerprintSerializer.LINE_END);
		assertEquals(
				lines[2] + ',',
				FingerprintSerializer.LINE_START
						+ FingerprintTest.FILE_2.getFile()
						+ FingerprintSerializer.LINE_MID
						+ FingerprintTest.FILE_2.getHash()
						+ FingerprintSerializer.LINE_END);
		assertEquals(lines[3], FingerprintSerializer.PAGE_END);
	}

	public void testRoundTrip() {
		Fingerprint print = FingerprintTest.create(FingerprintTest.FILE_1,
				FingerprintTest.FILE_2);
		FingerprintSerializer serializer = new FingerprintSerializer();
		String json = serializer.toString(print);
		Fingerprint print2 = serializer.fromString(json);
		assertEquals(print, print2);
	}
}
