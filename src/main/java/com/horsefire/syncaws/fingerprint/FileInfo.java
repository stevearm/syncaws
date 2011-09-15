package com.horsefire.syncaws.fingerprint;


public final class FileInfo implements Comparable<FileInfo> {

	private final String m_path;
	private final String m_md5;
	private final long m_bytes;
	private final String m_key;

	public FileInfo(String path, String md5, long bytes) {
		if (path == null || path.isEmpty() || md5 == null || md5.isEmpty()) {
			throw new NullPointerException();
		}
		m_path = path.replaceAll("\\\\", "/");
		m_md5 = md5;
		m_bytes = bytes;
		m_key = m_path + ':' + m_md5 + ':' + m_bytes;
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
		return m_key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != FileInfo.class) {
			return false;
		}
		return m_key.equals(((FileInfo) obj).m_key);
	}

	@Override
	public String toString() {
		return m_key;
	}

	public int compareTo(FileInfo o) {
		return m_key.compareTo(o.m_key);
	}
}
