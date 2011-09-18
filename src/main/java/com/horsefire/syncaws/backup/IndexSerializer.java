package com.horsefire.syncaws.backup;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.horsefire.syncaws.json.DateTimeTools;

public class IndexSerializer {

	private Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		DateTimeTools.add(builder);
		builder.registerTypeAdapter(UploadedFile.class,
				new UploadedFileDeserializer());
		return builder.create();
	}

	public Index load(Reader in) {
		return getGson().fromJson(in, Index.class);
	}

	public String save(Index index) {
		return getGson().toJson(index);
	}
}
