package com.socklabs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseMain {

	public static final Logger logger = LoggerFactory.getLogger(BaseMain.class);

	public static int availableProcessors = Runtime.getRuntime().availableProcessors();

	public static final int RING_SIZE = 1024 * 16;
	public static final int COUNT = 5000000;
	public static final int THREAD_COUNT = availableProcessors - 1;

	private static long startTime;

	abstract public void run();

	protected void start() {
		startTime = System.currentTimeMillis();
		logger.info("Starting at {}", startTime);
	}

	protected void stop() {
		final long stopTime = System.currentTimeMillis();
		logger.info("Stopping at {} ... {}", stopTime, stopTime - startTime);
	}

}
