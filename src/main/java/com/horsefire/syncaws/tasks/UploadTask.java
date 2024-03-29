package com.horsefire.syncaws.tasks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import com.horsefire.syncaws.fingerprint.InkStamp;

public class UploadTask implements Task {

	private static final Logger LOG = LoggerFactory.getLogger(UploadTask.class);

	private final CommandLineArgs m_args;
	private final ConfigService m_config;
	private final AwsClient m_awsClient;
	private final UrlService m_urlService;
	private final InkStamp m_inkStamp;
	private final FingerprintSerializer m_serializer;

	@Inject
	public UploadTask(CommandLineArgs options, ConfigService config,
			AwsClient awsClient, UrlService urlService, InkStamp inkStamp,
			FingerprintSerializer serializer) {
		m_args = options;
		m_config = config;
		m_awsClient = awsClient;
		m_urlService = urlService;
		m_inkStamp = inkStamp;
		m_serializer = serializer;
	}

	public void validate() {
		if (m_args.getArgs().size() != 1) {
			throw new RuntimeException("Task expects project name");
		}
	}

	public void updateFingerprint(Project selectedProject) {
		Fingerprint print = m_inkStamp.createNew(new File(selectedProject
				.getBaseDir()));
		String json = m_serializer.save(print);
		try {
			FileWriter writer = new FileWriter(
					selectedProject.getFingerprintFile(m_args));
			writer.write(json);
			writer.close();
			LOG.info("Local fingerprint updated");
		} catch (IOException e) {
			throw new RuntimeException("Error serializing out fingerprint", e);
		}
	}

	public void run() throws NoSuchAlgorithmException, S3ServiceException {
		Project selectedProject = m_config.getProject(m_args.getArgs().get(0));
		updateFingerprint(selectedProject);

		Delta delta = getDelta(selectedProject);
		List<UploadedFile> files = delta.getFilesToUpload();
		if (files.isEmpty()) {
			LOG.info("No new files. Backup is up-to-date");
			return;
		}
		LOG.info("{} new files to upload (total size {} bytes)", files.size(),
				delta.getTotalBytes());
		String indexName = uploadIndex(selectedProject, delta.getNewIndex());
		LOG.info("Uploaded new index: {}", indexName);
		uploadFiles(selectedProject, files);
		updateIndexList(selectedProject, indexName);
	}

	private Delta getDelta(Project selectedProject) {
		Fingerprint print = getFingerprint(selectedProject);
		Index index = getMostRecentIndex(selectedProject);
		return new DeltaGenerator(new UuidGenerator()).create(index, print);
	}

	private Fingerprint getFingerprint(Project selectedProject) {
		File file = selectedProject.getFingerprintFile(m_args);
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

	private String uploadIndex(Project selectedProject, Index index) {
		String indexJson = new IndexSerializer().save(index);
		DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyyMMddHHmm");
		String indexName = pattern.print(new DateTime(DateTimeZone.UTC))
				+ ".js";
		try {
			m_awsClient.putJson(indexJson,
					m_urlService.getIndex(selectedProject, indexName));
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

	private void uploadFiles(Project project, List<UploadedFile> files) {
		try {
			for (UploadedFile file : files) {
				File localFile = new File(project.getBaseDir(), file.getPath());
				LOG.info("Uploading {} ({} bytes)",
						localFile.getAbsolutePath(), file.getBytes());
				m_awsClient.putFile(localFile,
						m_urlService.getFile(project, file));
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
	private void updateIndexList(Project project, String indexName) {
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
}
