package com.nv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

	public static final Logger system = LoggerFactory.getLogger("system");
	
	public static final Logger gameInfoLogger = LoggerFactory.getLogger("gameInfoLogger");
	
	public static final Logger gameErrorLogger = LoggerFactory.getLogger("gameErrorLogger");

	public static final Logger err = LoggerFactory.getLogger("err");

	public static final Logger httpClient = LoggerFactory.getLogger("httpClient");

	public static final Logger webSocketEndPoint = LoggerFactory.getLogger("webSocketEndPoint");

	public static final Logger webSocket = LoggerFactory.getLogger("webSocket");

	public static final Logger messageQueue = LoggerFactory.getLogger("messageQueue");
	
	public static final Logger unitTest = LoggerFactory.getLogger("unitTest");
	
	// 記錄Servlet response回應錯誤，評估運作狀態使用
	public static final Logger responseError = LoggerFactory.getLogger("responseError");

	public static final Logger parseError = LoggerFactory.getLogger("parseError");

	public static final Logger gameShoeNoValidation = LoggerFactory.getLogger("gameShoeNoValidation");
	
	public static final Logger winnerValidation = LoggerFactory.getLogger("winnerValidation");
	
	public static final Logger skipRoundValidation = LoggerFactory.getLogger("skipRoundValidation");
	
	public static final Logger randomPayValidation = LoggerFactory.getLogger("randomPayValidation");
	
	public static final Logger oneCardDrawnValidation = LoggerFactory.getLogger("oneCardDrawnValidation");
	
	public static final Logger betTimeValidation = LoggerFactory.getLogger("betTimeValidation");
	
	public static final Logger betInsuranceValidation = LoggerFactory.getLogger("betInsuranceValidation");
	
	public static final Logger changeStateValidation = LoggerFactory.getLogger("changeStateValidation");
	
	public static final Logger voidRoundValidation = LoggerFactory.getLogger("voidRoundValidation");
	
	public static final Logger reshakeSicboValidation = LoggerFactory.getLogger("reshakeSicboValidation");
	
	public static final Logger betOneDayValidation = LoggerFactory.getLogger("betOneDayValidation");
	
	public static final Logger dealCardValidation = LoggerFactory.getLogger("dealCardValidation");

	public static final Logger redisSubscribe = LoggerFactory.getLogger("redisSubscribe");
	
	// 僅static function，不應該執行建構子
	private LogUtils() {
		throw new AssertionError();
	}
}