package com.nv.util;

import com.nv.commons.constant.Setting;

public class FrontendUtils extends com.nv.expandUtil.util.FrontendUtils {

	public static String getJQueryPath() {
		return "/js/jquery-3.2.1.min.js";
	}

	public static int getJsFileVersion() {
		return Setting.JS_FILE_VERSION;
	}
}