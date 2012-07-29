package com.socklabs;

import com.lmax.disruptor.EventFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ngerakines
 * Date: 7/29/12
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValueEvent {
	private long value;

	public ValueEvent() { }

	public ValueEvent(final long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ValueEvent{" +
				"value=" + value +
				'}';
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};

}
