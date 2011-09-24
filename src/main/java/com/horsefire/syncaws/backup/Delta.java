package com.horsefire.syncaws.backup;

import java.util.List;

public final class Delta {

	private final Index m_newIndex;
	private final List<UploadedFile> m_filesToUpload;

	private transient long m_totalBytes = 0;

	public Delta(Index newIndex, List<UploadedFile> filesToUpload) {
		m_newIndex = newIndex;
		m_filesToUpload = filesToUpload;
	}

	public Index getNewIndex() {
		return m_newIndex;
	}

	public List<UploadedFile> getFilesToUpload() {
		return m_filesToUpload;
	}

	public long getTotalBytes() {
		if (m_totalBytes == 0) {
			for (UploadedFile file : m_filesToUpload) {
				m_totalBytes += file.getBytes();
			}
		}
		return m_totalBytes;
	}
}
