package com.horsefire.syncaws;

import junit.framework.TestCase;

import org.junit.Test;

import com.horsefire.syncaws.NumberConverter;

public class NumberConverterTest extends TestCase {

	// private static final long TEST_VALUE_LONG = 76865768658765l;
	private static final byte[] TEST_VALUE_BYTES = new byte[] { 0x45,
			(byte) 0xE8, (byte) 0xB4, (byte) 0xD6, (byte) 0x17, (byte) 0x4D };
	private static final String TEST_VALUE_HEX = "45E8B4D6174D";

	@Test
	public void testBytesToHexStringConversion() {
		NumberConverter converter = new NumberConverter();
		assertEquals(TEST_VALUE_HEX, converter.toHexString(TEST_VALUE_BYTES));
	}

	@Test
	public void testHexStringToBytesConversion() {
		NumberConverter converter = new NumberConverter();
		byte[] result = converter.toByteArray(TEST_VALUE_HEX);
		for (int i = 0; i < TEST_VALUE_BYTES.length; i++) {
			assertEquals(TEST_VALUE_BYTES[i], result[i]);
		}
	}
}
