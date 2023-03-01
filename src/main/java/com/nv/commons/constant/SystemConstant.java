package com.nv.commons.constant;

import com.nv.manager.SystemInfo;

public class SystemConstant {
	
	public static final int SUCCESS_CODE = 200;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";

	public static final String NO_COUNTRY_RESULT = "XX";

	public static final String NO_REGION_RESULT = "XX";

	public static final String NO_CITY_RESULT = "XX";

	// 包含保留映射IPv6地址的IPv4地址 = 45個字符, ex.
	// ABCD:ABCD:ABCD:ABCD:ABCD:ABCD:192.168.158.190
	public static final int IP_VALID_MAX_LENGTH = 45;
	public static final String SYSTEM_NAME = "HttpProxy";
	/**
	 * Redis Prefix
	 *
	 * httpproxy:lcs
	 */
	public static final String REDIS_PREFIX = String
		.join(SystemConstant.REDIS_TAG, SYSTEM_NAME, SystemInfo.getInstance().getSiteNameType().name())
		.toLowerCase();

	// Redis Tag
	public static final String REDIS_TAG = ":";
	
	// DealerProxy event加解密鑰
	public static final String DEALER_EVENT_AES_TOKEN = "qwervbppnrvr56m123+#";
	
	public static final String URL_EVENT = "/event";
	public static final String URL_SETTING = "/setting";

}