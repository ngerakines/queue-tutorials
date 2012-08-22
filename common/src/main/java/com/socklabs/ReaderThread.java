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

	public static final ValueEvent SHUTDOWN_REQ = new ValueEvent();
	private volatile boolean shuttingDown, loggerTerminated;

	protected int count = 0;
	private final BlockingQueue<ValueEvent> theQueue;

	public ReaderThread(final BlockingQueue<ValueEvent> theQueue) {
		this.theQueue = theQueue;
	}

	public void run() {
		try {
			while (consume(theQueue.take())) {
				count++;
			}
		} catch (InterruptedException iex) {
			// Do nothing.
		} finally {
			loggerTerminated = true;
		}
	}

	private boolean consume(final ValueEvent valueEvent) {
		return valueEvent.getValue() != -1;
	}

	public void log(ValueEvent event) {
		if (shuttingDown || loggerTerminated) {
			return;
		}
		if (event == null) {
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
