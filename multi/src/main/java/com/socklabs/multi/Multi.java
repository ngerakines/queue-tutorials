package com.socklabs.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.socklabs.BaseMain;
import com.socklabs.ValueEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multi extends BaseMain {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		Disruptor<ValueEvent> disruptor =
				new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, executor,
						new MultiThreadedClaimStrategy(RING_SIZE),
						new SleepingWaitStrategy());
		disruptor.handleEventsWith(createEvents(THREAD_COUNT));
		RingBuffer<ValueEvent> ringBuffer = disruptor.start();

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

	public static EventHandler<ValueEvent>[] createEvents(final int count) {
		logger.info("Creating {} handlers", count);
		EventHandler<ValueEvent>[] events = new EventHandler[count];
		for (int i = 0; i < count; i++) {
			events[i] = new OrdHandler(i, count);
		}
		return events;
	}

}
