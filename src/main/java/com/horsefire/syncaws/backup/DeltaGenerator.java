package com.horsefire.syncaws.backup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.horsefire.syncaws.fingerprint.FileInfo;
import com.horsefire.syncaws.fingerprint.Fingerprint;

public class DeltaGenerator {

	private String getGUID() {
		return UUID.randomUUID().toString();
	}

	private UploadedFile convert(FileInfo fileInfo) {
		return new UploadedFile(fileInfo.getPath(), fileInfo.getMd5(),
				fileInfo.getBytes(), getGUID());
	}

	public Delta create(Index lastIndex, Fingerprint print) {
		List<UploadedFile> files = new ArrayList<UploadedFile>();
		List<UploadedFile> filesToUpload = new ArrayList<UploadedFile>();
		String lastIndexName = null;
		if (lastIndex != null) {
			List<FileInfo> newFiles = new ArrayList<FileInfo>(print.getFiles());
			List<UploadedFile> oldFiles = new ArrayList<UploadedFile>(
					lastIndex.getFiles());
			while (!oldFiles.isEmpty() && !newFiles.isEmpty()) {
				UploadedFile oldFile = oldFiles.get(0);
				if (oldFile.isSameFile(newFiles.get(0))) {
					files.add(oldFiles.remove(0));
					newFiles.remove(0);
					continue;
				}
				int newFileIndex = -1;
				for (int i = 0; i < newFiles.size(); i++) {
					if (oldFile.isSameFile(newFiles.get(i))) {
						newFileIndex = i;
						break;
					}
				}
				while (newFileIndex > 0) {
					UploadedFile uploadedFile = convert(newFiles.remove(0));
					files.add(uploadedFile);
					filesToUpload.add(uploadedFile);
					newFileIndex--;
				}
				if (newFileIndex == -1) {
					oldFiles.remove(0);
				}
			}
			if (!newFiles.isEmpty()) {
				for (FileInfo fileInfo : newFiles) {
					UploadedFile uploadedFile = convert(fileInfo);
					files.add(uploadedFile);
					filesToUpload.add(uploadedFile);
				}
			}
		} else {
			for (FileInfo file : print.getFiles()) {
				UploadedFile uploadedFile = convert(file);
				files.add(uploadedFile);
				filesToUpload.add(uploadedFile);
			}
		}

		Index newIndex = new Index(print.getGenerated(), lastIndexName, files);
		return new Delta(newIndex, filesToUpload);
	}
}
