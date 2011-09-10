package com.horsefire.syncaws.md5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class Fingerprint {

	private final List<Md5File> m_files;

	public Fingerprint(List<Md5File> files) {
		m_files = Collections.unmodifiableList(new ArrayList<Md5File>(files));
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
		throw new UnsupportedOperationException();
	}

	public Diff getDiff(Fingerprint other) {
		throw new UnsupportedOperationException();
	}

	public static final class Diff {
		public final List<Md5File> added = new ArrayList<Md5File>();
		public final List<Md5File> removed = new ArrayList<Md5File>();
	}
}
