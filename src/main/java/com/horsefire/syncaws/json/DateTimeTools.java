package com.horsefire.syncaws.json;

import java.lang.reflect.Type;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeTools {
	public static JsonSerializer<DateTime> SERIALIZER = new DateTimeSerializer();
	public static JsonDeserializer<DateTime> DESERIALIZER = new DateTimeDeserializer();

	public static void add(GsonBuilder builder) {
		builder.registerTypeAdapter(DateTime.class, SERIALIZER);
		builder.registerTypeAdapter(DateTime.class, DESERIALIZER);
	}

	public static DateTime parse(String dateTime) {
		return FORMATTER.parseDateTime(dateTime);
	}

	public static String render(DateTime dateTime) {
		return FORMATTER.print(dateTime);
	}

	private static final String PATTERN = "yyyy-MM-dd HH:mm";
	private static final DateTimeFormatter FORMATTER = DateTimeFormat
			.forPattern(PATTERN);

	private static class DateTimeSerializer implements JsonSerializer<DateTime> {

		public JsonElement serialize(DateTime src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(FORMATTER.print(src));
		}
	}

	private static class DateTimeDeserializer implements
			JsonDeserializer<DateTime> {
		public DateTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return FORMATTER.parseDateTime(json.getAsJsonPrimitive()
					.getAsString());
		}
	}
}