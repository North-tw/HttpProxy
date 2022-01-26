package com.nv.commons.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum SessionRemoveReasonType {

	Close(0), Error(1), InitException(2);

	public final static List<SessionRemoveReasonType> VALUES = Arrays.stream(SessionRemoveReasonType.values())
		.collect(Collectors.toUnmodifiableList());

	public static Optional<SessionRemoveReasonType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	public static Optional<SessionRemoveReasonType> get(String name) {
		return VALUES.stream().filter(e -> e.name().equalsIgnoreCase(name)).findFirst();
	}

	private final int unique;

	private SessionRemoveReasonType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
