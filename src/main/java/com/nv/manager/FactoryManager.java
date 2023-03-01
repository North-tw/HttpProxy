package com.nv.manager;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConstructorUtils;

import com.nv.manager.factory.IEventListenerFactory;

public class FactoryManager {

	private final static Object lock = new Object();

	private static FactoryManager factoryManager;

	private final IEventListenerFactory eventListenerFactory;

	private FactoryManager(IEventListenerFactory eventListenerFactory) {
		this.eventListenerFactory = eventListenerFactory;
	}

	public static void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
		InstantiationException, ClassNotFoundException {
		if (factoryManager == null) {
			synchronized (lock) {
				if (factoryManager == null) {
					String eventListenerFactoryClass = SystemInfo.getInstance()
						.getInitParameter("EventListenerFactoryClass");
					IEventListenerFactory eventListenerFactory = (IEventListenerFactory) ConstructorUtils
						.invokeConstructor(Class.forName(eventListenerFactoryClass), null);
					factoryManager = new FactoryManager(eventListenerFactory);
				}
			}
		}
	}

	public static FactoryManager getInstance() {
		return factoryManager;
	}

	public IEventListenerFactory getEventListenerFactory() {
		return eventListenerFactory;
	}

}
