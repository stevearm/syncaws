package com.horsefire.syncaws.backup;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.horsefire.syncaws.fingerprint.FileInfo;

public class IndexSerializer {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	private static final String KEY_GENERATED = "generated";
	private static final String KEY_LAST_INDEX = "lastIndex";
	private static final String KEY_FILES = "files";
	private static final String KEY_FILE_PATH = "path";
	private static final String KEY_FILE_BYTES = "bytes";
	private static final String KEY_FILE_URL = "url";
	private static final String KEY_FILE_MD5 = "md5";

	public String save(Index index) {
		JSONObject parent = new JSONObject();
		parent.put(
				KEY_GENERATED,
				DateTimeFormat.forPattern(DATE_FORMAT).print(
						index.getGenerated()));
		parent.put(KEY_LAST_INDEX, index.getLastIndex());
		JSONArray files = new JSONArray();
		parent.put(KEY_FILES, files);
		for (UploadedFile file : index.getFiles()) {
			JSONObject fileJson = new JSONObject();
			FileInfo fileInfo = file.getFileInfo();
			fileJson.put(KEY_FILE_PATH, fileInfo.getPath());
			fileJson.put(KEY_FILE_BYTES, new Long(fileInfo.getBytes()));
			fileJson.put(KEY_FILE_MD5, fileInfo.getMd5());
			fileJson.put(KEY_FILE_URL, file.getUrl());
			files.add(fileJson);
		}
		return parent.toJSONString();
	}

	private String getString(JSONObject object, String key, boolean nullOk) {
		Object obj = object.get(key);
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj == null && nullOk) {
			return null;
		}
		throw new ParseException("Expected value for " + key
				+ " to be a string: " + obj);
	}

	private DateTime getGeneration(JSONObject index) {
		String value = getString(index, KEY_GENERATED, false);
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
		try {
			return formatter.parseDateTime(value);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Could not parse generation time: "
					+ value);
		}
	}

	private UploadedFile parseFile(Object object) {
		if (!(object instanceof JSONObject)) {
			throw new ParseException("File must be a json object");
		}
		JSONObject file = (JSONObject) object;
		object = file.get(KEY_FILE_BYTES);
		if (!(object instanceof Number)) {
			throw new ParseException(KEY_FILE_BYTES
					+ " field must be an integer");
		}
		int bytes = ((Number) object).intValue();
		FileInfo fileInfo = new FileInfo(getString(file, KEY_FILE_PATH, false),
				getString(file, KEY_FILE_MD5, false), bytes);
		return new UploadedFile(fileInfo, getString(file, KEY_FILE_URL, false));
	}

	public Index load(Reader in) {
		Object object;
		try {
			object = new JSONParser().parse(in);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (org.json.simple.parser.ParseException e) {
			throw new ParseException(e);
		}
		if (!(object instanceof JSONObject)) {
			throw new ParseException("json was not an object");
		}
		JSONObject index = (JSONObject) object;

		DateTime generated = getGeneration(index);
		String previousBackup = getString(index, KEY_LAST_INDEX, true);

		object = index.get(KEY_FILES);
		if (!(object instanceof JSONArray)) {
			throw new ParseException("Index must contain an array at key: "
					+ KEY_FILES);
		}
		JSONArray filesJson = (JSONArray) object;
		List<UploadedFile> files = new ArrayList<UploadedFile>();
		for (Object file : filesJson) {
			files.add(parseFile(file));
		}
		return new Index(generated, previousBackup, files);
	}

	@SuppressWarnings("serial")
	public static final class ParseException extends RuntimeException {
		public ParseException(String message) {
			super(message);
		}

		public ParseException(Exception e) {
			super(e);
		}
	}
}
