package com.nv.commons.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum WebSocketMessageType {

	Error(0), Heartbeat(1),;

	public final static List<WebSocketMessageType> VALUES = Collections
		.unmodifiableList(Arrays.asList(WebSocketMessageType.values()));

	public static Optional<WebSocketMessageType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	public static Optional<WebSocketMessageType> get(String name) {
		return VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	WebSocketMessageType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
