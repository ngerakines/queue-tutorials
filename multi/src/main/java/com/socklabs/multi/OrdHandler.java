package com.socklabs.multi;

import com.lmax.disruptor.EventHandler;
import com.socklabs.BaseEventHandler;
import com.socklabs.ValueEvent;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/19/12
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrdHandler extends BaseEventHandler {
	private final long ordinal;
	private int handled_count = 1;
	private final long size;

	public OrdHandler(final long ordinal, final long size) {
		this.ordinal = ordinal;
		this.size = size;
	}

	@Override
	public void handle(final ValueEvent event, final long sequence, final boolean endOfBatch) {
		if ((sequence % size) == ordinal) {
			handled_count++;
		}
	}

	@Override
	protected void log(final ValueEvent event) {
		System.out.println("Batch " + ordinal + " size " + handled_count + " / " + event.toString() + " / " + System.currentTimeMillis());
	}

}
