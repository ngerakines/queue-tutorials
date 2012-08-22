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
		Bqexec bqexec = new Bqexec();
		bqexec.run();
	}

	@Override
	public void run() {

		final BlockingQueue<ValueEvent> theQueue = new ArrayBlockingQueue<ValueEvent>(BaseMain.RING_SIZE);

		final Bqrunner[] threads = new Bqrunner[THREAD_COUNT + 1];

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		for (int threadNumber = 0; threadNumber < THREAD_COUNT; threadNumber++) {
			threads[threadNumber] = new Bqrunner(theQueue);
			executor.execute(threads[threadNumber]);
		}

		start();

		long i = 0;
		try {
			for (i = 0; i < COUNT; i++) {
				theQueue.put(new ValueEvent(i));
			}
		} catch (InterruptedException e) {
			System.out.println("Failed on put " + i);
			e.printStackTrace();
		}

		for (int threadNumber = 0; threadNumber < THREAD_COUNT; threadNumber++) {
			theQueue.add(new ValueEvent(-1));
		}

		// This will make the executor accept no new threads
		// and finish all existing threads in the queue
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}

		stop();
	}
}
