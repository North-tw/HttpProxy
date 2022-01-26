package com.nv.commons.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum WebSocketRequestType {

	heartbeat(0), record(1);

	public final static List<WebSocketRequestType> VALUES = Arrays.stream(WebSocketRequestType.values())
		.collect(Collectors.toUnmodifiableList());

	public static Optional<WebSocketRequestType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	public static Optional<WebSocketRequestType> get(String name) {
		return VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	private WebSocketRequestType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
