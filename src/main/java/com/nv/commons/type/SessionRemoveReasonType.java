package com.nv.commons.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum SessionRemoveReasonType {

	Close(0), Error(1), InitException(2);

	public final static List<SessionRemoveReasonType> VALUES = Collections
		.unmodifiableList(Arrays.asList(SessionRemoveReasonType.values()));

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
