package com.nv.commons.listener;

import com.nv.commons.type.GameEventType;

public interface IEventListener {

	void handleEvent(String message) throws Exception;

	GameEventType getGameEvent();

}
