package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.CommandLineArgs;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.UuidGenerator;
import com.horsefire.syncaws.aws.AwsClient;
import com.horsefire.syncaws.aws.UrlService;
import com.horsefire.syncaws.backup.Delta;
import com.horsefire.syncaws.backup.DeltaGenerator;
import com.horsefire.syncaws.backup.Index;
import com.horsefire.syncaws.backup.IndexSerializer;
import com.horsefire.syncaws.backup.UploadedFile;
import com.horsefire.syncaws.fingerprint.Fingerprint;
import com.horsefire.syncaws.fingerprint.FingerprintSerializer;

public class UploadTask extends ProjectTask {

	private static final Logger LOG = LoggerFactory.getLogger(UploadTask.class);
	private final AwsClient m_awsClient;
	private final UrlService m_urlService;

	@Inject
	public UploadTask(CommandLineArgs options, ConfigService config,
			AwsClient awsClient, UrlService urlService) {
		super(options, config);
		m_awsClient = awsClient;
		m_urlService = urlService;
	}

	private Fingerprint getFingerprint() {
		File file = getSelectedProject().getFingerprintFile(getOptions());
		if (!file.isFile()) {
			throw new RuntimeException("Index does not exist at " + file);
		}
		try {
			FileReader reader = new FileReader(file);
			FingerprintSerializer serializer = new FingerprintSerializer();
			Fingerprint print = serializer.load(reader);
			reader.close();
			return print;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private JSONArray getIndexList(Project project) {
		try {
			Reader reader = m_awsClient.getReader(m_urlService
					.getIndexList(project));
			Object object = new JSONParser().parse(reader);
			reader.close();
			if (!(object instanceof JSONArray)) {
				throw new RuntimeException("Error deserializing index list");
			}
			return (JSONArray) object;
		} catch (ServiceException e) {
			throw new RuntimeException("Error reading index list from S3", e);
		} catch (IOException e) {
			throw new RuntimeException("Error reading index list from S3", e);
		} catch (ParseException e) {
			throw new RuntimeException("Error reading index list from S3", e);
		}
	}

	private Index getMostRecentIndex(Project project) {
		try {
			JSONArray indexes = getIndexList(project);
			if (indexes.isEmpty()) {
				return null;
			}
			Object object = indexes.get(indexes.size() - 1);
			if (!(object instanceof String)) {
				throw new RuntimeException("Index must be a string");
			}
			Reader reader = m_awsClient.getReader(m_urlService.getIndex(
					project, (String) object));
			Index index = new IndexSerializer().load(reader);
			reader.close();
			return index;
		} catch (S3ServiceException e) {
			throw new RuntimeException("Error loading index", e);
		} catch (ServiceException e) {
			throw new RuntimeException("Error loading index", e);
		} catch (IOException e) {
			throw new RuntimeException("Error loading index", e);
		}
	}

	private Delta getDelta() {
		Fingerprint print = getFingerprint();
		Index index = getMostRecentIndex(getSelectedProject());
		return new DeltaGenerator(new UuidGenerator()).create(index, print);
	}

	private String uploadIndex(Index index) {
		String indexJson = new IndexSerializer().save(index);
		DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyyMMddHHmm");
		String indexName = pattern.print(new DateTime(DateTimeZone.UTC))
				+ ".js";
		try {
			m_awsClient.putJson(indexJson,
					m_urlService.getIndex(getSelectedProject(), indexName));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error uploading index", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error uploading index", e);
		} catch (S3ServiceException e) {
			throw new RuntimeException("Error uploading index", e);
		} catch (IOException e) {
			throw new RuntimeException("Error uploading index", e);
		}
		return indexName;
	}

	private void uploadFiles(List<UploadedFile> files) {
		try {
			Project project = getSelectedProject();
			for (UploadedFile file : files) {
				File localFile = new File(project.getBaseDir(), file.getPath());
				LOG.info("Uploading {} ({} bytes)",
						localFile.getAbsolutePath(), file.getBytes());
				m_awsClient.putFile(localFile, m_urlService.getFile(
						getSelectedProject(), file.getId()));
			}
		} catch (S3ServiceException e) {
			throw new RuntimeException("Error uploading file", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error uploading file", e);
		} catch (IOException e) {
			throw new RuntimeException("Error uploading file", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateIndexList(String indexName) {
		Project project = getSelectedProject();
		JSONArray indexList = getIndexList(project);
		indexList.add(indexName);
		try {
			m_awsClient.putJson(indexList.toString(),
					m_urlService.getIndexList(project));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error updating index list", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error updating index list", e);
		} catch (S3ServiceException e) {
			throw new RuntimeException("Error updating index list", e);
		} catch (IOException e) {
			throw new RuntimeException("Error updating index list", e);
		}
	}

	public void run() throws NoSuchAlgorithmException, S3ServiceException {
		Delta delta = getDelta();
		List<UploadedFile> files = delta.getFilesToUpload();
		if (files.isEmpty()) {
			LOG.info("No new files. Backup is up-to-date");
			return;
		}
		LOG.info("{} new files to upload (total size {} bytes)", files.size(),
				delta.getTotalBytes());
		if (!getOptions().isDryrun()) {
			String indexName = uploadIndex(delta.getNewIndex());
			uploadFiles(files);
			updateIndexList(indexName);
		}
	}
}
