package com.horsefire.syncaws;

import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public final class SyncAws {

	private static class Options {

		@Parameter
		public List<String> parameters = Lists.newArrayList();
	}

	public static void main(String[] args) {
		Options options = new Options();
		new JCommander(options, args);
	}
}