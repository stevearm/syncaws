package com.horsefire.syncaws.backup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public final class Index {

	@SerializedName("generated")
	private final DateTime m_generated;

	@SerializedName("lastIndex")
	private final String m_lastIndex;

	@SerializedName("files")
	private final List<UploadedFile> m_files;

	public Index(DateTime generated, String lastIndex, List<UploadedFile> files) {
		m_generated = generated;
		m_lastIndex = lastIndex;
		List<UploadedFile> fileList = new ArrayList<UploadedFile>(files);
		Collections.sort(fileList);
		m_files = fileList;
	}

	public DateTime getGenerated() {
		return m_generated;
	}

	public String getLastIndex() {
		return m_lastIndex;
	}

	public List<UploadedFile> getFiles() {
		return m_files;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(m_generated).append(m_lastIndex)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != Index.class) {
			return false;
		}
		final Index that = (Index) obj;
		return new EqualsBuilder().append(m_generated, that.m_generated)
				.append(m_lastIndex, that.m_lastIndex)
				.append(m_files, that.m_files).isEquals();
	}

	@Override
	public String toString() {
		return "Index:" + m_generated;
	}
}
