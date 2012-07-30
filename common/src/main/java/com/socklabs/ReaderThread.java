package com.socklabs;

import com.socklabs.BaseMain;
import com.socklabs.ValueEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReaderThread extends Thread {

	public static final ValueEvent SHUTDOWN_REQ = new ValueEvent(-1);
	private volatile boolean shuttingDown, loggerTerminated;

	protected int count = 0;
	private final BlockingQueue<ValueEvent> theQueue;

	public ReaderThread(final BlockingQueue<ValueEvent> theQueue) {
		this.theQueue = theQueue;
	}

	public void run() {
		try {
			ValueEvent item;
			while ((item = theQueue.take()).getValue() != -1) {
				count++;
			}
		} catch (InterruptedException iex) {
		} finally {
			loggerTerminated = true;
		}
	}

	public void log(ValueEvent event) {
		if (shuttingDown || loggerTerminated) {
			return;
		}
		try {
			theQueue.put(event);
		} catch (InterruptedException iex) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Unexpected interruption");
		}
	}

	public void shutDown() throws InterruptedException {
		shuttingDown = true;
		theQueue.put(SHUTDOWN_REQ);
	}
}
