package com.horsefire.syncaws.fingerprint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public final class Fingerprint {

	@SerializedName("generated")
	private final DateTime m_generated;

	@SerializedName("files")
	private final List<FileInfo> m_files;

	public Fingerprint(DateTime generated, List<FileInfo> files) {
		m_generated = generated;
		ArrayList<FileInfo> list = new ArrayList<FileInfo>(files);
		Collections.sort(list);
		m_files = Collections.unmodifiableList(list);
	}

	public DateTime getGenerated() {
		return m_generated;
	}

	public List<FileInfo> getFiles() {
		return m_files;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(m_files).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != Fingerprint.class) {
			return false;
		}
		final Fingerprint that = (Fingerprint) obj;
		return new EqualsBuilder().append(m_files, that.m_files).isEquals();
	}

	@Override
	public String toString() {
		return "Fingerprint:" + m_files.size() + "files";
	}

	public String showFiles() {
		StringBuilder result = new StringBuilder();
		for (FileInfo file : m_files) {
			result.append(file);
			result.append('\n');
		}
		return result.toString();
	}

	public Diff diff(Fingerprint other) {
		List<FileInfo> removed = new ArrayList<FileInfo>();
		List<FileInfo> added = new ArrayList<FileInfo>();

		List<FileInfo> oldFiles = new ArrayList<FileInfo>(m_files);
		List<FileInfo> newFiles = new ArrayList<FileInfo>(other.m_files);
		while (!oldFiles.isEmpty() && !newFiles.isEmpty()) {
			FileInfo oldFile = oldFiles.get(0);
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
		List<FileInfo> addedFiles();

		List<FileInfo> removedFiles();
	}

	private static final class DiffImpl implements Diff {
		private final List<FileInfo> m_added;
		private final List<FileInfo> m_removed;

		public DiffImpl(List<FileInfo> added, List<FileInfo> removed) {
			Collections.sort(added);
			m_added = Collections.unmodifiableList(added);
			Collections.sort(removed);
			m_removed = Collections.unmodifiableList(removed);
		}

		public List<FileInfo> addedFiles() {
			return m_added;
		}

		public List<FileInfo> removedFiles() {
			return m_removed;
		}
	}
}
