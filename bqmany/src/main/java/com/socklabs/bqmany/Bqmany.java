package com.socklabs.bqmany;

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
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bqmany extends BaseMain {

	public static void main(String[] args) throws InterruptedException {
		Bqmany bqmany = new Bqmany();
		bqmany.run();
	}

	@Override
	public void run() {

		final BlockingQueue<ValueEvent> theQueue = new ArrayBlockingQueue<ValueEvent>(BaseMain.RING_SIZE);

		int[] counts = new int[THREAD_COUNT];
		final ReaderThread[] threads = new ReaderThread[THREAD_COUNT];
		for (int threadNumber = 0; threadNumber < THREAD_COUNT; threadNumber++) {
			threads[threadNumber] = new ReaderThread(theQueue);
			threads[threadNumber].start();
			counts[threadNumber] = 0;
		}

		start();

		for (long i = 0; i < COUNT; i++) {
			threads[((int) (i % threads.length))].log(new ValueEvent(i));
			counts[((int) (i % threads.length))]++;
		}

		/* for (int threadNumber = 0; threadNumber < THREAD_COUNT; threadNumber++) {
			System.out.println("count for " + threadNumber + " is " + counts[threadNumber]);
		} */

		try {
			for (int threadNumber = 0; threadNumber < THREAD_COUNT; threadNumber++) {
				threads[threadNumber].shutDown();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stop();
	}

}
