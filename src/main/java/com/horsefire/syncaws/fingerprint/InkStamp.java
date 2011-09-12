package com.horsefire.syncaws.fingerprint;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;
import com.horsefire.syncaws.fingerprint.DirectoryWalker.FileProcessor;

public class InkStamp {

	private final Md5Calculator m_md5Calculator;
	private final DirectoryWalker<Md5File> m_dirWalker;

	@Inject
	public InkStamp(Md5Calculator md5Calculator,
			DirectoryWalker<Md5File> dirWalker) {
		m_md5Calculator = md5Calculator;
		m_dirWalker = new DirectoryWalker<Md5File>();
	}

	public Fingerprint createNew(final File dir) {
		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Can only fingerprint folders");
		}
		final String baseline = dir.getAbsolutePath();
		List<Md5File> list = m_dirWalker.walkDir(dir,
				new FileProcessor<Md5File>() {
					public Md5File process(File file) throws Exception {
						String filepath = file.getAbsolutePath();
						if (filepath.startsWith(baseline)) {
							filepath = filepath.substring(baseline.length() + 1);
						}
						return new Md5File(filepath, m_md5Calculator
								.getMd5String(file));
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
