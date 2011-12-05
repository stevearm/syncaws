package com.horsefire.syncaws.fingerprint;

import java.io.File;
import java.util.List;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.horsefire.syncaws.fingerprint.DirectoryWalker.FileProcessor;
import com.horsefire.util.FileDelegate;

public class InkStamp {

	private final Md5Calculator m_md5Calculator;
	private final DirectoryWalker<FileInfo> m_dirWalker;
	private final FileDelegate m_fileDelegate;

	@Inject
	public InkStamp(Md5Calculator md5Calculator,
			DirectoryWalker<FileInfo> dirWalker, FileDelegate fileDelegate) {
		m_md5Calculator = md5Calculator;
		m_dirWalker = new DirectoryWalker<FileInfo>();
		m_fileDelegate = fileDelegate;
	}

	public Fingerprint createNew(final File dir) {
		if (!m_fileDelegate.isDirectory(dir)) {
			throw new IllegalArgumentException("Can only fingerprint folders");
		}
		final DateTime generationTime = new DateTime();
		final String baseline = m_fileDelegate.getAbsolutePath(dir);
		List<FileInfo> list = m_dirWalker.walkDir(dir,
				new FileProcessor<FileInfo>() {
					public FileInfo process(File file) throws Exception {
						String filepath = file.getAbsolutePath();
						if (filepath.startsWith(baseline)) {
							filepath = filepath.substring(baseline.length() + 1);
						}
						return new FileInfo(filepath, m_md5Calculator
								.getMd5String(file), file.length());
					}
				});
		return new Fingerprint(generationTime, list);
	}
}
