package com.nv.commons.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 交易類的狀態
 * 
 * @author SYSTEM
 * 
 */
public enum ServerInfoType {

	PROXY_SERVER(1), MANAGER(4),;

	public final static List<ServerInfoType> VALUES = Collections
		.unmodifiableList(Arrays.asList(ServerInfoType.values()));

	public static Optional<ServerInfoType> get(int unique) {
		return VALUES.stream().filter(e -> e.unique() == unique).findFirst();
	}

	private int value;

	private ServerInfoType(int value) {
		this.value = value;
	}

	public int unique() {
		return value;
	}
}
