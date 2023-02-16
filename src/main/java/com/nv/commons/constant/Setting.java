package com.nv.commons.constant;


public class Setting {

	// JavaScript File Version
	public static int JS_FILE_VERSION = 0;
	
	// 重開ProxyServer前先停止接受事件，由另外一臺ProxyServer囘覆DealerProxy，確保事件不遺漏。
	public static boolean STOP_RECEIVE_DEALER_EVENT = false;

}