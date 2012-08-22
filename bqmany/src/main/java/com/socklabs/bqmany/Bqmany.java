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

		final ReaderThread[] threads = new ReaderThread[] {
				new ReaderThread(theQueue),
				new ReaderThread(theQueue),
				new ReaderThread(theQueue)
		};

		threads[0].start();
		threads[1].start();
		threads[2].start();

		start();

		for (long i = 0; i < COUNT; i++) {
			threads[((int) (i % threads.length))].log(new ValueEvent(i));
		}

		try {
			threads[0].shutDown();
			threads[1].shutDown();
			threads[2].shutDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stop();
	}

}
