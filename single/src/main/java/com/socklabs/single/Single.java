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
		final ExecutorService executor = Executors.newSingleThreadExecutor();

		final Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(
				ValueEvent.EVENT_FACTORY, executor,
				new SingleThreadedClaimStrategy(RING_SIZE),
				new SleepingWaitStrategy());

		disruptor.handleEventsWith(handler);

		final RingBuffer<ValueEvent> ringBuffer = disruptor.start();

		long start = System.currentTimeMillis();
		logger.info("Starting at {}", start);

		for (long i = 0; i < COUNT; i++) {
			long sequence = ringBuffer.next();
			ValueEvent event = ringBuffer.get(sequence);
			event.setValue(i);
			ringBuffer.publish(sequence);
		}

		long stop = System.currentTimeMillis();
		logger.info("Stopping at {} ... {}", stop, stop - start);
	}

	final static EventHandler<ValueEvent> handler = new BaseEventHandler() {

		@Override
		public void handle(ValueEvent event, long sequence, boolean endOfBatch) {
			return;
		}

	};

}
