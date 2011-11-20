package com.horsefire.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;

/**
 * This class exists to do all the things that java.io.File does itself. Using
 * this class allows tests to pass in a mock, allowing the test to mimic a file
 * or directory existing
 */
public class FileDelegate {

	public String getName(File file) {
		return file.getName();
	}

	public String getParent(File file) {
		return file.getParent();
	}

	public File getParentFile(File file) {
		return file.getParentFile();
	}

	public String getPath(File file) {
		return file.getPath();
	}

	public boolean isAbsolute(File file) {
		return file.isAbsolute();
	}

	public String getAbsolutePath(File file) {
		return file.getAbsolutePath();
	}

	public File getAbsoluteFile(File file) {
		return file.getAbsoluteFile();
	}

	public String getCanonicalPath(File file) throws IOException {
		return file.getCanonicalPath();
	}

	public File getCanonicalFile(File file) throws IOException {
		return file.getCanonicalFile();
	}

	public URI toURI(File file) {
		return file.toURI();
	}

	public boolean canRead(File file) {
		return file.canRead();
	}

	public boolean canWrite(File file) {
		return file.canWrite();
	}

	public boolean exists(File file) {
		return file.exists();
	}

	public boolean isDirectory(File file) {
		return file.isDirectory();
	}

	public boolean isFile(File file) {
		return file.isFile();
	}

	public boolean isHidden(File file) {
		return file.isHidden();
	}

	public long lastModified(File file) {
		return file.lastModified();
	}

	public long length(File file) {
		return file.length();
	}

	public boolean createNewFile(File file) throws IOException {
		return file.createNewFile();
	}

	public boolean delete(File file) {
		return file.delete();
	}

	public void deleteOnExit(File file) {
		file.deleteOnExit();
	}

	public String[] list(File file) {
		return file.list();
	}

	public String[] list(File file, FilenameFilter filter) {
		return file.list(filter);
	}

	public File[] listFiles(File file) {
		return file.listFiles();
	}

	public File[] listFiles(File file, FilenameFilter filter) {
		return file.listFiles(filter);
	}

	public File[] listFiles(File file, FileFilter filter) {
		return file.listFiles(filter);
	}

	public boolean mkdir(File file) {
		return file.mkdir();
	}

	public boolean mkdirs(File file) {
		return file.mkdirs();
	}

	public boolean renameTo(File file, File dest) {
		return file.renameTo(dest);
	}

	public boolean setLastModified(File file, long time) {
		return file.setLastModified(time);
	}

	public boolean setReadOnly(File file) {
		return file.setReadOnly();
	}

	public boolean setWritable(File file, boolean writable, boolean ownerOnly) {
		return file.setWritable(writable, ownerOnly);
	}

	public boolean setWritable(File file, boolean writable) {
		return file.setWritable(writable);
	}

	public boolean setReadable(File file, boolean readable, boolean ownerOnly) {
		return file.setReadable(readable, ownerOnly);
	}

	public boolean setReadable(File file, boolean readable) {
		return file.setReadable(readable);
	}

	public boolean setExecutable(File file, boolean executable,
			boolean ownerOnly) {
		return file.setExecutable(executable, ownerOnly);
	}

	public boolean setExecutable(File file, boolean executable) {
		return file.setExecutable(executable);
	}

	public boolean canExecute(File file) {
		return file.canExecute();
	}

	public long getTotalSpace(File file) {
		return file.getTotalSpace();
	}

	public long getFreeSpace(File file) {
		return file.getFreeSpace();
	}

	public long getUsableSpace(File file) {
		return file.getUsableSpace();
	}
}
