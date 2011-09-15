package com.horsefire.syncaws.backup;

import com.horsefire.syncaws.fingerprint.FileInfo;

public final class UploadedFile implements Comparable<UploadedFile> {

	private final FileInfo m_fileInfo;
	private final String m_url;
	private final String m_key;

	public UploadedFile(FileInfo fileInfo, String url) {
		m_fileInfo = fileInfo;
		m_url = url;
		m_key = m_fileInfo.toString() + ':' + m_url;
	}

	public FileInfo getFileInfo() {
		return m_fileInfo;
	}

	public String getUrl() {
		return m_url;
	}

	@Override
	public int hashCode() {
		return m_key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != UploadedFile.class) {
			return false;
		}
		return m_key.equals(((UploadedFile) obj).m_key);
	}

	@Override
	public String toString() {
		return m_key;
	}

	public int compareTo(UploadedFile o) {
		return m_key.compareTo(o.m_key);
	}
}
