package com.horsefire.syncaws.fingerprint;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.horsefire.syncaws.json.DateTimeTools;

public class FingerprintSerializer {

	private Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		DateTimeTools.add(builder);
		builder.registerTypeAdapter(FileInfo.class, new FileInfoDeserializer());
		return builder.create();
	}

	public Fingerprint load(Reader in) {
		return getGson().fromJson(in, Fingerprint.class);
	}

	public String save(Fingerprint fingerprint) {
		return getGson().toJson(fingerprint);
	}
}
