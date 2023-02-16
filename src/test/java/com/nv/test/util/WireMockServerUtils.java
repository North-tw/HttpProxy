package com.nv.test.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.tomakehurst.wiremock.http.Request;
import com.nv.util.StringUtils;

public class WireMockServerUtils {

	public static Map<String, String> getParameter(Request req) {
		return Arrays.stream(StringUtils.split(req.getBodyAsString(), "&"))
			.map(str -> StringUtils.split(str, "="))
			.collect(Collectors.toMap(a -> a[0], a -> a[1]));
	}
}
