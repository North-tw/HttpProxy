package com.nv.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.nv.commons.constant.SystemConstant;
import com.nv.expandUtil.util.StringUtils;

public class Validator {

	private static Map<String, Pattern> patterns = new HashMap<>();

	// Url: start-http:// or https://，不允許特殊字元
	private static final Pattern urlPattern = Pattern.compile(
		"^(http(s)?:\\/\\/)?(www.)?([a-zA-Z0-9])+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z0-9]{2,5}(:[0-9]{1,5})?(\\/[^\\s]*)?$");

	private static final Pattern[] xssPatterns = new Pattern[]{
		// Script fragments
		Pattern.compile("&lt;script&gt;(.*?)&lt;/script&gt;", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
		// src='...'
		Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		// lonely script tags
		Pattern.compile("&lt;/script&gt;", Pattern.CASE_INSENSITIVE),
		Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
		Pattern.compile("&lt;script(.*?)&gt;", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		// eval(...)
		Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		// expression(...)
		Pattern.compile("expression\\((.*?)\\)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
		// javascript:...
		Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
		// vbscript:...
		Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
		// onload(...)=...
		Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)};

	public static Pattern getPattern(String patternStr) {
		Pattern pattern = patterns.get(patternStr);
		if (pattern == null) {
			pattern = Pattern.compile(patternStr);
			patterns.put(patternStr, pattern);
		}
		return pattern;
	}

	public static boolean isMatched(String patternStr, String value) {
		return getPattern(patternStr).matcher(value).matches();
	}

	public static boolean isMatched(Pattern pattern, String value) {
		return pattern.matcher(value).matches();
	}

	public static String stripXSS(String value) {
		if (value != null) {

			value = value.replaceAll("\0", "");

			for (Pattern scriptPattern : xssPatterns) {
				value = scriptPattern.matcher(value).replaceAll("");
			}
		}
		return value;
	}

	public static boolean isIpAddress(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}

		if (value.length() > SystemConstant.IP_VALID_MAX_LENGTH) {
			value = value.substring(0, SystemConstant.IP_VALID_MAX_LENGTH);
		}

		if ("127.0.0.1".equals(value)) {
			return false;
		}

		// 24位元區塊 - 10.0.0.0 ~ 10.255.255.255
		if (isMatched(
			"^(10\\.)((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){2}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
			value)) {
			return false;
		}

		// 20位元區塊 - 172.16.0.0 ~ 172.31.255.255
		if (isMatched(
			"^(172\\.)(1[6-9]|2[0-9]|3[0-1]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
			value)) {
			return false;
		}

		// 16位元區塊 - 192.168.0.0 ~ 192.168.255.255
		if (isMatched(
			"^(192\\.168\\.)(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
			value)) {
			return false;
		}

		return isMatched(
			"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", value);
	}

	public static boolean isValidatedUrl(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		return isMatched(urlPattern, value);
	}
}
