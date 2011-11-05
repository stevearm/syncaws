package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.aws.AwsClient;
import com.horsefire.syncaws.backup.Delta;
import com.horsefire.syncaws.backup.DeltaGenerator;
import com.horsefire.syncaws.backup.Index;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;

public class DeltaTask extends ProjectTask {

	private static final Logger LOG = LoggerFactory.getLogger(DeltaTask.class);

	@Inject
	public DeltaTask(CommandLineArgs options, ConfigService config) {
		super(options, config);
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

	public void run() {
		Fingerprint print = getFingerprint();
		try {
			Index index = new AwsClient(getConfig())
					.getMostRecentIndex(getSelectedProject());
			Delta delta = new DeltaGenerator().create(index, print);
			LOG.info("Should upload: " + delta.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
