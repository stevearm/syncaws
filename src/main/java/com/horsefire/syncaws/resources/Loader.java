package com.horsefire.syncaws.resources;

import java.io.InputStreamReader;
import java.io.Reader;

import com.horsefire.syncaws.aws.UrlService;

public class Loader {

	public Reader getHtmlIndexFile() {
		return new InputStreamReader(this.getClass().getResourceAsStream(
				UrlService.HTML_FILE));
	}
}
