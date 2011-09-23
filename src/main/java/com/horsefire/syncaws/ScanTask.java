package com.horsefire.syncaws;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.horsefire.syncaws.SyncAws.Options;
import com.horsefire.syncaws.fingerprint.DirectoryWalker;
import com.horsefire.syncaws.fingerprint.FileInfo;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;
import com.horsefire.syncaws.fingerprint.InkStamp;
import com.horsefire.syncaws.fingerprint.Md5Calculator;
import com.horsefire.syncaws.fingerprint.NumberConverter;

public class ScanTask extends Task {

	public ScanTask(Options options, Config config) {
		super(options, config);
	}

	@Override
	public void validate() {
		requireSelectedProject();
	}

	@Override
	public void run() {
		Project selectedProject = getSelectedProject();

		final Md5Calculator md5Calculator = new Md5Calculator(
				new NumberConverter());
		final DirectoryWalker<FileInfo> walker = new DirectoryWalker<FileInfo>();
		final InkStamp stamp = new InkStamp(md5Calculator, walker);

		FingerprintSerializer serializer = new FingerprintSerializer();
		Fingerprint print = stamp.createNew(new File(selectedProject
				.getBaseDir()));
		String json = serializer.save(print);
		try {
			FileWriter writer = new FileWriter(new File(getOptions().configDir,
					selectedProject.getId() + ".js"));
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Error serializing out fingerprint", e);
		}
	}
}
