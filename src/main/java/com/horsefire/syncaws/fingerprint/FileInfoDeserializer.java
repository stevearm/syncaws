package com.horsefire.syncaws.fingerprint;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class FileInfoDeserializer implements JsonDeserializer<FileInfo> {
	public FileInfo deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (!json.isJsonObject()) {
			throw new JsonParseException("File info should always be an object");
		}
		JsonObject object = json.getAsJsonObject();

		JsonElement element = object.get("path");
		if (element == null || !element.isJsonPrimitive()
				|| !element.getAsJsonPrimitive().isString()
				|| element.getAsString().isEmpty()) {
			throw new JsonParseException(
					"File info must have a path as a string");
		}
		String path = element.getAsString();

		element = object.get("md5");
		if (element == null || !element.isJsonPrimitive()
				|| !element.getAsJsonPrimitive().isString()
				|| element.getAsString().isEmpty()) {
			throw new JsonParseException(
					"File info must have an md5 as a string");
		}
		String md5 = element.getAsString();

		element = object.get("bytes");
		if (element == null || !element.isJsonPrimitive()
				|| !element.getAsJsonPrimitive().isNumber()) {
			throw new JsonParseException(
					"Uploaded file must have bytes as an integer");
		}
		long bytes = element.getAsLong();

		return new FileInfo(path, md5, bytes);
	}
}
