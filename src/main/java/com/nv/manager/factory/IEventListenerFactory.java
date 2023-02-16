package com.nv.manager.factory;

import java.util.Optional;

import com.nv.commons.listener.IEventListener;

public interface IEventListenerFactory {
	Optional<IEventListener> get(int gameEventCode);
}
