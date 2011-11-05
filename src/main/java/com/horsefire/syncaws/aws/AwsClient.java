package com.horsefire.syncaws.aws;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;
import com.horsefire.syncaws.backup.Index;
import com.horsefire.syncaws.backup.IndexSerializer;

public class AwsClient {

	private final ConfigService m_config;
	private transient AWSCredentials m_credentials;
	private transient S3Service m_service;

	@Inject
	public AwsClient(ConfigService config) {
		m_config = config;
	}

	private AWSCredentials getCredentials() {
		if (m_credentials == null) {
			m_credentials = new AWSCredentials(m_config.getAccessKey(),
					m_config.getSecretAccessKey());
		}
		return m_credentials;
	}

	private S3Service getService() throws S3ServiceException {
		if (m_service == null) {
			m_service = new RestS3Service(getCredentials());
		}
		return m_service;
	}

	public void testConnection() throws S3ServiceException {
		getService().getBucket(m_config.getBucket());
	}

	private String getProjectUrl(Project project) {
		return m_config.getBaseUrl() + '/' + project.getId();
	}

	public boolean checkProject(Project project) throws S3ServiceException {
		try {
			getService().getObject(m_config.getBucket(),
					getProjectUrl(project) + "/indexes.js");
			return true;
		} catch (S3ServiceException e) {
			if (e.getResponseCode() == 404) {
				return false;
			}
			throw e;
		}
	}

	public Index getMostRecentIndex(Project project) throws IOException,
			ParseException {
		try {
			S3Object indexJson = getService().getObject(m_config.getBucket(),
					getProjectUrl(project) + "/indexes.js");
			Object object = new JSONParser().parse(new InputStreamReader(
					indexJson.getDataInputStream()));
			if (!(object instanceof JSONArray)) {
				throw new RuntimeException("Error deserializing index list");
			}
			JSONArray indexes = (JSONArray) object;
			if (indexes.isEmpty()) {
				return null;
			}
			object = indexes.get(indexes.size() - 1);
			if (!(object instanceof String)) {
				throw new RuntimeException("Index must be a string");
			}
			S3Object json = getService().getObject(m_config.getBucket(),
					getProjectUrl(project) + "/" + (String) object);
			return new IndexSerializer().load(new InputStreamReader(json
					.getDataInputStream()));
		} catch (S3ServiceException e) {
			throw new RuntimeException("Error loading index", e);
		} catch (ServiceException e) {
			throw new RuntimeException("Error loading index", e);
		}
	}

	public List<String> getProjects() throws S3ServiceException {
		S3Object[] objects = getService().listObjects(m_config.getBucket(),
				m_config.getBaseUrl(), null);

		// Print out each object's key and size.
		for (int o = 0; o < objects.length; o++) {
			System.out.println(" " + objects[o].getKey() + " ("
					+ objects[o].getContentLength() + " bytes)");
		}
		return null;
	}
}
