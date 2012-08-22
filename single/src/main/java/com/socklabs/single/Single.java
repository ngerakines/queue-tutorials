package com.socklabs.single;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.socklabs.BaseEventHandler;
import com.socklabs.BaseMain;
import com.socklabs.ValueEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Single extends BaseMain {

	public static void main(String[] args) {
		Single single = new Single();
		single.run();
	}

	public void run() {
		final ExecutorService executor = Executors.newSingleThreadExecutor();

		final Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(
				ValueEvent.EVENT_FACTORY, executor,
				new SingleThreadedClaimStrategy(RING_SIZE),
				new SleepingWaitStrategy());

		disruptor.handleEventsWith(handler);

		final RingBuffer<ValueEvent> ringBuffer = disruptor.start();

		start();

		for (long i = 0; i < COUNT; i++) {
			final long sequence = ringBuffer.next();
			final ValueEvent event = ringBuffer.get(sequence);
			event.setValue(i);
			ringBuffer.publish(sequence);
		}

		disruptor.shutdown();
		executor.shutdown();
		stop();
	}

	final static EventHandler<ValueEvent> handler = new BaseEventHandler() {

		@Override
		public void handle(ValueEvent event, long sequence, boolean endOfBatch) {
			// Do nothing.
		}

	};

}
