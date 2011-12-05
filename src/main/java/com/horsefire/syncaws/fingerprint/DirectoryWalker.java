package com.horsefire.syncaws.fingerprint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryWalker<T> {

	private static final Logger LOG = LoggerFactory
			.getLogger(DirectoryWalker.class);

	public static interface FileProcessor<T> {
		T process(File file) throws Exception;
	}

	public List<T> walkDir(File dir, FileProcessor<T> processor) {
		ExecutorService executor = new ThreadPoolExecutor(1, 4, 0,
				TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());

		List<Future<T>> futures = new ArrayList<Future<T>>();
		LOG.debug("Walking directory: {}", dir);
		walkDir(dir, futures, processor, executor);
		LOG.debug("Found {} files", futures.size());
		List<T> result = new ArrayList<T>(futures.size());
		for (Future<T> future : futures) {
			try {
				result.add(future.get());
			} catch (InterruptedException e) {
				LOG.error("Failure processing file", e);
			} catch (ExecutionException e) {
				LOG.error("Failure processing file", e);
			}
		}
		LOG.debug("Done processing files");
		executor.shutdown();
		return result;
	}

	private void walkDir(File dir, List<Future<T>> results,
			FileProcessor<T> processor, ExecutorService executor) {
		if (dir.isFile()) {
			results.add(executor.submit(new ProcessedFile(dir, processor)));
		} else if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				walkDir(file, results, processor, executor);
			}
		} else {
			LOG.error(
					"Cannot process something that's not a file or a directory: {}",
					dir);
		}
	}

	private class ProcessedFile implements Callable<T> {

		private final File m_file;
		private final FileProcessor<T> m_processor;

		public ProcessedFile(File file, FileProcessor<T> processor) {
			m_file = file;
			m_processor = processor;
		}

		public T call() throws Exception {
			return m_processor.process(m_file);
		}
	}
}
