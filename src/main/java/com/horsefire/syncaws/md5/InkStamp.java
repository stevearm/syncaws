package com.horsefire.syncaws.md5;

import java.io.File;
import java.util.List;

import com.horsefire.syncaws.md5.Md5Engine.FileNameConverter;

public class InkStamp {

	private final Md5Engine m_engine;

	public InkStamp(Md5Engine engine) {
		m_engine = engine;
	}

	public Fingerprint createNew(final File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Can only fingerprint folders");
		}
		final String baseline = dir.getAbsolutePath();
		System.out.println("Baseline: " + baseline);
		List<Md5File> list = m_engine.walkDir(dir, new FileNameConverter() {
			public String getFileName(File file) {
				String filepath = file.getAbsolutePath();
				if (filepath.startsWith(baseline)) {
					return filepath.substring(baseline.length() + 1);
				}
				return filepath;
			}
		});
		return new Fingerprint(list);
	}

	public void save(Fingerprint fingerprint, File dir) {
		throw new UnsupportedOperationException();
	}

	public Fingerprint loadExisting(File dir) {
		throw new UnsupportedOperationException();
	}
}
