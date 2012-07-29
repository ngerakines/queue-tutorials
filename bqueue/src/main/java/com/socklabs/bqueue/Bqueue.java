package com.socklabs.bqueue;

import com.socklabs.BaseMain;
import com.socklabs.ReaderThread;
import com.socklabs.ValueEvent;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bqueue extends BaseMain {

	public static void main(String[] args) throws InterruptedException {
		final ExecutorService executor = Executors.newSingleThreadExecutor();

		long start = System.currentTimeMillis();
		logger.info("Starting at {}", start);

		final BlockingQueue<ValueEvent> theQueue = new ArrayBlockingQueue<ValueEvent>(BaseMain.RING_SIZE);

		ReaderThread readerThread = new ReaderThread(theQueue);
		readerThread.start();

		for (long i = 0; i < COUNT; i++) {
			readerThread.log(new ValueEvent(i));
		}

		readerThread.shutDown();

		long stop = System.currentTimeMillis();
		logger.info("Stopping at {} ... {}", stop, stop - start);
	}
}
