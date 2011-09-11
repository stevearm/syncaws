package com.horsefire.syncaws.md5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Fingerprint {

	private final List<Md5File> m_files;

	public Fingerprint(List<Md5File> files) {
		ArrayList<Md5File> list = new ArrayList<Md5File>(files);
		Collections.sort(list);
		m_files = Collections.unmodifiableList(list);
	}

	public List<Md5File> getFiles() {
		return m_files;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != Fingerprint.class) {
			return false;
		}
		final Fingerprint that = (Fingerprint) obj;
		if (m_files.size() != that.m_files.size()) {
			return false;
		}
		Iterator<Md5File> it = that.m_files.iterator();
		for (Md5File file : m_files) {
			if (!file.equals(it.next())) {
				return false;
			}
		}
		return true;
	}

	public String showFiles() {
		StringBuilder result = new StringBuilder();
		for (Md5File file : m_files) {
			result.append(file);
			result.append('\n');
		}
		return result.toString();
	}

	public Diff getDiff(Fingerprint other) {
		throw new UnsupportedOperationException();
	}

	public static final class Diff {
		public final List<Md5File> added = new ArrayList<Md5File>();
		public final List<Md5File> removed = new ArrayList<Md5File>();
	}
}
