package com.horsefire.syncaws;

public class Config {

	String accessKey = "s3AccessKey";

	String secretAccessKey = "s3SecretKey";

	String bucket = "myBucketName";

	String baseUrl = "myfiles/backups";

	Project[] projects;

	public Config() {
	}

	public Config(Config config, Project[] projects) {
		this.accessKey = config.accessKey;
		this.secretAccessKey = config.secretAccessKey;
		this.bucket = config.bucket;
		this.baseUrl = config.baseUrl;
		this.projects = projects;
	}
}
