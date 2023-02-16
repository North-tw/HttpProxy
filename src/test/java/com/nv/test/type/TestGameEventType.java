package com.nv.test.type;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

import com.nv.commons.listener.IEventListener;
import com.nv.commons.type.GameEventType;
import com.nv.test.listener.TestEventListener;

/**
 * Only use for dealer connection.
 *
 * @author SONY
 * 
 */
public enum TestGameEventType implements IEventListener {

	GP_TEST(999, new TestEventListener());

	private final static TestGameEventType[] VALUES;

	static {
		int maxCode = Arrays.stream(TestGameEventType.values()).mapToInt(e -> e.code).max().getAsInt();

		VALUES = new TestGameEventType[maxCode + 1];
		for (TestGameEventType e : TestGameEventType.values()) {
			VALUES[e.code] = e;
		}
	}

	private final int code;

	private IEventListener eventListener;

	private TestGameEventType(int code, IEventListener eventListener) {
		this.code = code;
		this.eventListener = eventListener;
	}

	public int getCode() {
		return code;
	}

	public static Optional<TestGameEventType> getInstanceOf(int code) {
		if (ArrayUtils.isArrayIndexValid(VALUES, code)) {
			return Optional.ofNullable(VALUES[code]);
		} else {
			return Optional.empty();
		}
	}

	public IEventListener getEventListener() {
		return this.eventListener;
	}

	public void afterEventProcessed() {

	}

	public int getRetryTimes() {
		return 3;
	}

	@Override
	public void handleEvent(String message) throws Exception {
		eventListener.handleEvent(message);
	}

	@Override
	public GameEventType getGameEvent() {
		return eventListener.getGameEvent();
	}

}
