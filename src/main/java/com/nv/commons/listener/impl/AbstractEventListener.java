package com.nv.commons.listener.impl;

import com.nv.commons.listener.IEventListener;
import com.nv.commons.type.GameEventType;

public abstract class AbstractEventListener implements IEventListener {

	private GameEventType gameEventType;

	public AbstractEventListener(GameEventType gameEventType) {
		this.gameEventType = gameEventType;
	}

	@Override
	public GameEventType getGameEvent() {
		return gameEventType;
	}

}
