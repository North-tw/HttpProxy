package com.nv.util;

public class ExceptionUtils extends com.nv.expandUtil.util.ExceptionUtils {

	private ExceptionUtils() {
		throw new AssertionError();
	}

	public static String createErrorMsg(Throwable t, String uuid) {
		return String.format("error message: %s, uuid: %s", t.getMessage(), uuid);
	}
}
