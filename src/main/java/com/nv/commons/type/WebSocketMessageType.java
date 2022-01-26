package com.nv.commons.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum WebSocketMessageType {

	Error(0), Heartbeat(1),;

	public final static List<WebSocketMessageType> VALUES = Arrays.stream(WebSocketMessageType.values())
		.collect(Collectors.toUnmodifiableList());

	public static Optional<WebSocketMessageType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	public static Optional<WebSocketMessageType> get(String name) {
		return VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	private WebSocketMessageType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
