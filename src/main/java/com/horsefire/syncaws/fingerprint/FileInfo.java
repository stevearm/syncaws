package com.horsefire.syncaws.fingerprint;

import com.google.gson.annotations.SerializedName;

public final class FileInfo implements Comparable<FileInfo> {

	@SerializedName("path")
	private final String m_path;

	@SerializedName("md5")
	private final String m_md5;

	@SerializedName("bytes")
	private final long m_bytes;

	private transient String m_key;

	public FileInfo(String path, String md5, long bytes) {
		if (path == null || path.isEmpty() || md5 == null || md5.isEmpty()) {
			throw new NullPointerException();
		}
		m_path = path.replaceAll("\\\\", "/");
		m_md5 = md5;
		m_bytes = bytes;
	}

	private String getKey() {
		if (m_key == null) {
			m_key = m_path + ':' + m_md5 + ':' + m_bytes;
		}
		return m_key;
	}

	public String getPath() {
		return m_path;
	}

	public String getMd5() {
		return m_md5;
	}

	public long getBytes() {
		return m_bytes;
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != FileInfo.class) {
			return false;
		}
		return getKey().equals(((FileInfo) obj).getKey());
	}

	@Override
	public String toString() {
		return getKey();
	}

	public int compareTo(FileInfo o) {
		return getKey().compareTo(o.getKey());
	}
}
