package com.horsefire.syncaws.md5;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Engine {

	public static final FileNameConverter FILE_CONVERTER_ABSOLUTE = new FileNameConverter() {
		public String getFileName(File file) {
			return file.getAbsolutePath();
		}
	};

	private static final Logger LOG = LoggerFactory.getLogger(Md5Engine.class);

	private final Md5Calculator m_md5Calculator;

	public Md5Engine(Md5Calculator md5Calculator) {
		m_md5Calculator = md5Calculator;
	}

	public List<Md5File> walkDir(File dir) {
		return walkDir(dir, FILE_CONVERTER_ABSOLUTE);
	}

	public List<Md5File> walkDir(File dir, FileNameConverter converter) {
		Map<String, Future<Md5File>> futures = new HashMap<String, Future<Md5File>>();
		ExecutorService executor = new ThreadPoolExecutor(1, 4, 0,
				TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
		walkDir(dir, executor, futures, converter);
		List<String> files = new ArrayList<String>(futures.keySet());
		Collections.sort(files);
		List<Md5File> result = new ArrayList<Md5File>(files.size());
		for (String file : files) {
			try {
				result.add(futures.get(file).get());
			} catch (InterruptedException e) {
				LOG.error("Failure getting md5", e);
			} catch (ExecutionException e) {
				LOG.error("Failure getting md5", e);
			}
		}
		return result;
	}

	private void walkDir(File dir, ExecutorService executor,
			Map<String, Future<Md5File>> results, FileNameConverter converter) {
		if (!dir.isDirectory()) {
			return;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				results.put(converter.getFileName(file),
						executor.submit(new Md5FileCallable(file, converter)));
			} else if (file.isDirectory()) {
				walkDir(file, executor, results, converter);
			} else {
				LOG.error("Cannot hash something that's not a file or a directory: "
						+ file);
			}
		}
	}

	public static interface FileNameConverter {
		String getFileName(File file);
	}

	private class Md5FileCallable implements Callable<Md5File> {

		private final File m_file;
		private final FileNameConverter m_converter;

		public Md5FileCallable(File file, FileNameConverter converter) {
			m_file = file;
			m_converter = converter;
		}

		public Md5File call() throws Exception {
			return new Md5File(m_converter.getFileName(m_file),
					m_md5Calculator.getMd5String(m_file));
		}
	}
}
