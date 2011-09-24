package com.horsefire.syncaws;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.horsefire.syncaws.SyncAws.Options;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;

public class DeltaTask extends Task {

	public DeltaTask(Options options, Config config) {
		super(options, config);
	}

	@Override
	public void validate() {
		requireSelectedProject();
	}

	private Fingerprint getFingerprint() {
		File file = getSelectedProject().getFingerprintFile(getOptions());
		if (!file.isFile()) {
			throw new RuntimeException("Index does not exist at " + file);
		}
		try {
			FileReader reader = new FileReader(file);
			FingerprintSerializer serializer = new FingerprintSerializer();
			Fingerprint print = serializer.load(reader);
			reader.close();
			return print;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		Fingerprint print = getFingerprint();
	}
}
