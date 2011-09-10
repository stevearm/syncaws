package com.horsefire.syncaws.md5;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.inject.Inject;
import com.horsefire.syncaws.util.NumberConverter;

public class Md5Calculator {

	private final NumberConverter m_converter;

	@Inject
	public Md5Calculator(NumberConverter converter) {
		m_converter = converter;
	}

	public String getMd5String(File file) throws IOException {
		return m_converter.toHexString(getMd5(file));
	}

	public byte[] getMd5(File file) throws IOException {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buffer = new byte[50];
			int length = 0;
			do {
				length = in.read(buffer);
				digest.update(buffer, 0, length);
			} while (length == buffer.length);
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException(
					"Somehow, MD5 is not supported", e);
		}
	}

	public String getMd5String(String... strings) {
		return m_converter.toHexString(getMd5(strings));
	}

	public byte[] getMd5(String... strings) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			for (String string : strings) {
				digest.update(string.getBytes());
			}
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new UnsupportedOperationException(
					"Somehow, MD5 is not supported", e);
		}
	}
}
