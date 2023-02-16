package com.nv.util;

import java.util.HashMap;
import java.util.Map;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.properties.ResultServerPropertiesView;
import com.nv.manager.cache.properties.ResultServerCache;

public class EventUtils {

	// 給game event service 使用
	private static final String ENCRYPT_TOKEN = SystemConstant.DEALER_EVENT_AES_TOKEN;

	private EventUtils() {
		throw new AssertionError();
	}

	public static Map<String, String> createParamsMap(String action, String message, int tableId) {
		ResultServerPropertiesView resultServerProperties = ResultServerCache.getInstance().get();
		
		try {
			message = EncryptUtils.desedeEncoder(message, ENCRYPT_TOKEN);
		} catch (Exception ex) {
			LogUtils.gameErrorLogger.error(ex.getMessage(), ex);
		}
		try {
			action = EncryptUtils.desedeEncoder(action, ENCRYPT_TOKEN);
		} catch (Exception ex) {
			LogUtils.gameErrorLogger.error(ex.getMessage(), ex);
		}
		final Map<String, String> params = new HashMap<String, String>();
		// domain code 是 ResultServer 設定好用來過濾 Request 的 token.
		params.put("domain", resultServerProperties.getDomainCode());
		params.put("tableID", String.valueOf(tableId));
		params.put("x", message);
		params.put("x1", action);
		return params;
	}
}
