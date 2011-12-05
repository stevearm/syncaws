package com.horsefire.syncaws.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.horsefire.syncaws.ConfigService;
import com.horsefire.syncaws.Project;

public class AwsClient {

	private static final Logger LOG = LoggerFactory.getLogger(AwsClient.class);

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

	private S3Object getObject(String url) throws S3ServiceException {
		return getService().getObject(m_config.getBucket(), url);
	}

	public Reader getReader(String url) throws ServiceException {
		LOG.debug("Reading file from {}", url);
		S3Object json = getObject(url);
		return new InputStreamReader(json.getDataInputStream());
	}

	public void putFile(File localFile, String url) throws S3ServiceException,
			NoSuchAlgorithmException, IOException {
		LOG.debug("Uploading file {} to {}", localFile, url);
		S3Object object = new S3Object(localFile);
		object.setKey(url);
		object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
		object.setContentDisposition("attachment; filename="
				+ localFile.getName());
		getService().putObject(m_config.getBucket(), object);
	}

	public void putJson(String json, String url)
			throws NoSuchAlgorithmException, UnsupportedEncodingException,
			IOException, S3ServiceException {
		LOG.debug("Uploading json content to {}", url);
		S3Object object = new S3Object(url, json.getBytes("UTF-8"));
		object.setContentType("application/javascript");
		object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
		getService().putObject(m_config.getBucket(), object);
	}

	public void putHtml(String html, String url)
			throws NoSuchAlgorithmException, UnsupportedEncodingException,
			IOException, S3ServiceException {
		LOG.debug("Uploading html content to {}", url);
		S3Object object = new S3Object(url, html.getBytes("UTF-8"));
		object.setContentType("text/html");
		object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
		getService().putObject(m_config.getBucket(), object);
	}

	public void deleteFile(String url) throws S3ServiceException,
			ServiceException {
		LOG.debug("Deleting file at {}", url);
		getService().deleteObject(m_config.getBucket(), url);
	}
}
