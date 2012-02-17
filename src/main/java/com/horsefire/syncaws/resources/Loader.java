package com.horsefire.syncaws.resources;

import java.io.InputStreamReader;
import java.io.Reader;

public class Loader {

	public Reader getHtmlIndexFile() {
		return new InputStreamReader(this.getClass().getResourceAsStream(
				"index.html"));
	}
}
