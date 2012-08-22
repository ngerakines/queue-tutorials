package com.socklabs.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.socklabs.BaseMain;
import com.socklabs.ValueEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multi extends BaseMain {

	public static void main(String[] args) {
		Multi multi = new Multi();
		multi.run();
	}

	public static EventHandler<ValueEvent>[] createEvents(final int count) {
		logger.info("Creating {} handlers", count);
		final EventHandler[] events = new EventHandler[count];
		for (int i = 0; i < count; i++) {
			events[i] = new OrdHandler(i, count);
		}
		return events;
	}

	@Override
	public void run() {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

		Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(
				ValueEvent.EVENT_FACTORY, executor,
				new MultiThreadedClaimStrategy(RING_SIZE),
				new SleepingWaitStrategy());

		disruptor.handleEventsWith(createEvents(THREAD_COUNT));
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

}
