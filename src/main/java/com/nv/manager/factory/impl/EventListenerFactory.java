package com.nv.manager.factory.impl;

import java.util.Optional;

import com.nv.commons.listener.IEventListener;
import com.nv.commons.type.GameEventType;
import com.nv.manager.factory.IEventListenerFactory;

public class EventListenerFactory implements IEventListenerFactory {
	
	
	/**
	 * call by reflection
	 * 
	 * @see com.nv.manager.FactoryManager#init
	 */
	public EventListenerFactory() {
	}

	@Override
	public Optional<IEventListener> get(int gameEventCode) {
		Optional<GameEventType> gameEvent = GameEventType.getInstanceOf(gameEventCode);
		if (gameEvent.isPresent()) {
			return Optional.ofNullable(gameEvent.get());
		} else {
			return Optional.empty();
		}
	}

}
