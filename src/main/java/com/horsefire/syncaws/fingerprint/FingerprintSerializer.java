package com.horsefire.syncaws.fingerprint;

import java.util.ArrayList;
import java.util.List;

public class FingerprintSerializer {

	@SuppressWarnings("serial")
	public static final class SerializingException extends RuntimeException {
		public SerializingException(String errorMessage) {
			super(errorMessage);
		}
	}

	static final String PAGE_START = "{\"files\":[";
	static final String PAGE_END = "]}";
	static final String LINE_START = "{\"filename\":\"";
	static final String LINE_MID = "\",\"hash\":\"";
	static final String LINE_END = "\"},";

	public String toString(Fingerprint print) {
		StringBuilder result = new StringBuilder(PAGE_START);
		List<Md5File> files = print.getFiles();
		for (Md5File file : files) {
			String filename = file.getFile();
			if (filename.indexOf('\\') != -1) {
				throw new SerializingException(
						"Filename contains illegal character: " + filename);
			}
			result.append('\n').append(LINE_START).append(filename)
					.append(LINE_MID).append(file.getHash()).append(LINE_END);
		}
		if (!files.isEmpty()) {
			result.setLength(result.length() - 1);
		}
		return result.append('\n').append(PAGE_END).toString();
	}

	public Fingerprint fromString(String json) {
		String[] lines = json.split("\\n");
		if (!lines[0].equals(PAGE_START)) {
			throw new SerializingException("First line does not look valid");
		}
		if (!lines[lines.length - 1].equals(PAGE_END)) {
			throw new SerializingException("Last line does not look valid");
		}
		List<Md5File> files = new ArrayList<Md5File>();
		for (int i = 1; i < lines.length - 1; i++) {
			String line = lines[i];
			if (!line.startsWith(LINE_START)) {
				throw new SerializingException("Line doesn't start correctly: "
						+ line);
			}

			if (i == lines.length - 2) {
				line = line + ',';
			}
			if (!line.endsWith(LINE_END)) {
				throw new SerializingException("Line doesn't end correctly: "
						+ line);
			}
			line = line.substring(LINE_START.length(),
					line.length() - LINE_END.length());
			int index = line.indexOf(LINE_MID);
			if (index == -1) {
				throw new SerializingException("Line doesn't contain hash tag");
			}
			files.add(new Md5File(line.substring(0, index), line
					.substring(index + LINE_MID.length())));
		}
		return new Fingerprint(files);
	}
}
