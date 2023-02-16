package com.nv.test.factory.impl;

import java.util.Optional;

import com.nv.commons.listener.IEventListener;
import com.nv.manager.factory.IEventListenerFactory;
import com.nv.test.type.TestGameEventType;

public class TestEventListenerFactory implements IEventListenerFactory {

	/**
	 * call by reflection
	 * 
	 * @see com.nv.manager.FactoryManager#init
	 */
	public TestEventListenerFactory() {
	}

	@Override
	public Optional<IEventListener> get(int gameEventCode) {
		Optional<TestGameEventType> gameEvent = TestGameEventType.getInstanceOf(gameEventCode);
		if (gameEvent.isPresent()) {
			return Optional.ofNullable(gameEvent.get());
		} else {
			return Optional.empty();
		}
	}

}
