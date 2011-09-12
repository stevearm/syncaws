package com.horsefire.syncaws.fingerprint;

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
		// This is obviously terribly inefficient, but since I'll never be
		// putting this file into a hash map, I just wanted this to be correct,
		// and didn't bother with fast
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

	public Diff diff(Fingerprint other) {
		List<Md5File> removed = new ArrayList<Md5File>();
		List<Md5File> added = new ArrayList<Md5File>();

		List<Md5File> oldFiles = new ArrayList<Md5File>(m_files);
		List<Md5File> newFiles = new ArrayList<Md5File>(other.m_files);
		while (!oldFiles.isEmpty() && !newFiles.isEmpty()) {
			Md5File oldFile = oldFiles.get(0);
			if (oldFile.equals(newFiles.get(0))) {
				oldFiles.remove(0);
				newFiles.remove(0);
				continue;
			}
			int newFileIndex = -1;
			for (int i = 0; i < newFiles.size(); i++) {
				if (oldFile.equals(newFiles.get(i))) {
					newFileIndex = i;
					break;
				}
			}
			while (newFileIndex > 0) {
				added.add(newFiles.remove(0));
				newFileIndex--;
			}
			if (newFileIndex == -1) {
				removed.add(oldFiles.remove(0));
			}
		}
		if (!oldFiles.isEmpty()) {
			removed.addAll(oldFiles);
		}
		if (!newFiles.isEmpty()) {
			added.addAll(newFiles);
		}

		return new DiffImpl(added, removed);
	}

	public static interface Diff {
		List<Md5File> addedFiles();

		List<Md5File> removedFiles();
	}

	private static final class DiffImpl implements Diff {
		private final List<Md5File> m_added;
		private final List<Md5File> m_removed;

		public DiffImpl(List<Md5File> added, List<Md5File> removed) {
			Collections.sort(added);
			m_added = Collections.unmodifiableList(added);
			Collections.sort(removed);
			m_removed = Collections.unmodifiableList(removed);
		}

		public List<Md5File> addedFiles() {
			return m_added;
		}

		public List<Md5File> removedFiles() {
			return m_removed;
		}
	}
}
