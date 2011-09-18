package com.horsefire.syncaws.backup;

import com.google.gson.annotations.SerializedName;

public final class UploadedFile implements Comparable<UploadedFile> {

	@SerializedName("path")
	private final String m_path;

	@SerializedName("md5")
	private final String m_md5;

	@SerializedName("bytes")
	private final long m_bytes;

	@SerializedName("id")
	private final String m_id;

	private transient String m_key;

	public UploadedFile(String path, String md5, long bytes, String id) {
		m_path = path;
		m_md5 = md5;
		m_bytes = bytes;
		m_id = id;
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

	public String getId() {
		return m_id;
	}

	private String getKey() {
		if (m_key == null) {
			m_key = m_path + ':' + m_md5 + ':' + m_bytes + ':' + m_id;
		}
		return m_key;
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != UploadedFile.class) {
			return false;
		}
		return getKey().equals(((UploadedFile) obj).getKey());
	}

	@Override
	public String toString() {
		return getKey();
	}

	public int compareTo(UploadedFile o) {
		return getKey().compareTo(o.getKey());
	}
}
