package com.nv.commons.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum WebSocketRequestType {

	heartbeat(0), record(1);

	public final static List<WebSocketRequestType> VALUES = Collections
		.unmodifiableList(Arrays.asList(WebSocketRequestType.values()));

	public static Optional<WebSocketRequestType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	public static Optional<WebSocketRequestType> get(String name) {
		return VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	WebSocketRequestType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
