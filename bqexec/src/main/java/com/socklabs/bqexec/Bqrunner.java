package com.socklabs.bqexec;

import com.socklabs.ValueEvent;

import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bqrunner implements Runnable {
	private final BlockingQueue<ValueEvent> queue;
	protected int count = 0;

	Bqrunner(final BlockingQueue<ValueEvent> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (consume(queue.take())) {
				count++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean consume(final ValueEvent valueEvent) {
		return valueEvent.getValue() != -1;
	}

}
