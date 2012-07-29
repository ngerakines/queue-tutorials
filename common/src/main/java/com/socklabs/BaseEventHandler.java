package com.socklabs;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseEventHandler implements EventHandler<ValueEvent> {

	public static final Logger logger = LoggerFactory.getLogger(BaseEventHandler.class);

	protected int count = 0;

	@Override
	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) throws Exception {
		count++;
		handle(event, sequence, endOfBatch);
		if (endOfBatch) {
			log(event);
			count = 0;
		}
	}

	protected void log(final ValueEvent event) {
		logger.info("Batch size {} / {} / {}", new Object[]{ count, event, System.currentTimeMillis()});
	}

	abstract public void handle(final ValueEvent event, final long sequence, final boolean endOfBatch);

}
