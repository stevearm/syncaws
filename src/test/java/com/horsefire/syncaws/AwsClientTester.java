package com.horsefire.syncaws;

import java.io.File;

import com.horsefire.syncaws.aws.AwsClient;

public class AwsClientTester {

	public static void main(String[] args) throws Exception {
		CommandLineArgs cmdLineArgs = new CommandLineArgs(
				"c:/users/steve/.syncaws", null, false, null, null);
		ConfigService config = new ConfigService(cmdLineArgs);
		AwsClient client = new AwsClient(config);
		String url = "backups/steve/kdjfoaisnaoivnawove2";
		client.putFile(new File(
				"c:/users/steve/downloads/application package.pdf"), url);
		// "src/main/java/com/horsefire/syncaws/ConfigService.java"), url);
		// client.deleteFile(url);
	}
}
