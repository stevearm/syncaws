package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;
import com.horsefire.syncaws.fingerprint.InkStamp;

public class ScanTask extends ProjectTask {

	private static final Logger LOG = LoggerFactory.getLogger(ScanTask.class);

	private final InkStamp m_inkStamp;
	private final FingerprintSerializer m_serializer;

	@Inject
	public ScanTask(CommandLineArgs options, ConfigService config,
			InkStamp inkStamp, FingerprintSerializer fingerprintSerializer) {
		super(options, config);
		m_inkStamp = inkStamp;
		m_serializer = fingerprintSerializer;
	}

	public void run() {
		Project selectedProject = getSelectedProject();

		Fingerprint print = m_inkStamp.createNew(new File(selectedProject
				.getBaseDir()));
		String json = m_serializer.save(print);
		try {
			FileWriter writer = new FileWriter(getSelectedProject()
					.getFingerprintFile(getOptions()));
			writer.write(json);
			writer.close();
			LOG.info("Local fingerprint updated");
		} catch (IOException e) {
			throw new RuntimeException("Error serializing out fingerprint", e);
		}
	}
}
