package com.horsefire.syncaws.md5;

public final class Md5File implements Comparable<Md5File> {

	private final String m_file;
	private final String m_hash;

	public Md5File(String file, String hash) {
		if (file == null || file.isEmpty() || hash == null || hash.isEmpty()) {
			throw new NullPointerException();
		}
		m_file = file.replaceAll("\\\\", "/");
		m_hash = hash;
	}

	public String getFile() {
		return m_file;
	}

	public String getHash() {
		return m_hash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + m_file.hashCode();
		result = prime * result + m_hash.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Md5File) {
			final Md5File other = (Md5File) obj;
			return m_file.equals(other.m_file) && m_hash.equals(other.m_hash);
		}
		return false;
	}

	@Override
	public String toString() {
		return m_hash + ':' + m_file;
	}

	public int compareTo(Md5File o) {
		if (o == null) {
			return 1;
		}
		return m_file.compareTo(o.m_file);
	}
}
