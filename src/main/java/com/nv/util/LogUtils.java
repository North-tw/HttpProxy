package com.nv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

	public static final Logger system = LoggerFactory.getLogger("system");

	public static final Logger err = LoggerFactory.getLogger("err");

	public static final Logger httpClient = LoggerFactory.getLogger("httpClient");

	public static final Logger webSocketEndPoint = LoggerFactory.getLogger("webSocketEndPoint");

	public static final Logger webSocket = LoggerFactory.getLogger("webSocket");

	public static final Logger messageQueue = LoggerFactory.getLogger("messageQueue");
	
	public static final Logger unitTest = LoggerFactory.getLogger("unitTest");

	// 僅static function，不應該執行建構子
	private LogUtils() {
		throw new AssertionError();
	}
}