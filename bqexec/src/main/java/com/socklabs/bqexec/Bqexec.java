package com.socklabs.bqexec;

import com.socklabs.BaseMain;
import com.socklabs.ReaderThread;
import com.socklabs.ValueEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bqexec extends BaseMain {

	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		logger.info("Starting at {}", start);

		final BlockingQueue<ValueEvent> theQueue = new ArrayBlockingQueue<ValueEvent>(BaseMain.RING_SIZE);

		final Bqrunner[] threads = new Bqrunner[] {
				new Bqrunner(theQueue),
				new Bqrunner(theQueue),
				new Bqrunner(theQueue)
		};

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		executor.execute(threads[0]);
		executor.execute(threads[1]);
		executor.execute(threads[2]);

		for (long i = 0; i < COUNT; i++) {
			theQueue.put(new ValueEvent(i));
		}

		theQueue.add(new ValueEvent(-1));
		theQueue.add(new ValueEvent(-1));
		theQueue.add(new ValueEvent(-1));

		// This will make the executor accept no new threads
		// and finish all existing threads in the queue
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}

		long stop = System.currentTimeMillis();
		logger.info("Stopping at {} ... {}", stop, stop - start);
	}
}
