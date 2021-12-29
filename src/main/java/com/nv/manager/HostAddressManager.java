package com.nv.manager;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.nv.expandUtil.util.StringUtils;
import com.nv.util.Validator;

public class HostAddressManager {

	private static final HostAddressManager instance = new HostAddressManager();

	public static final HostAddressManager getInstance() {
		return instance;
	}

	private HostAddressManager() {
		super();
	}

	public String getRealIPAddresses(HttpServletRequest httpRequest) {
		// incapsula, cdNetwork, akami
		String[] headers = new String[]{"x-forwarded-for", "x-forwarded-ip", "true-client-ip"};
		for (String key : headers) {
			String value = httpRequest.getHeader(key);
			if (value == null) {
				continue;
			}
			int clientIpIndex = value.indexOf(",");
			if (clientIpIndex != -1) {
				value = value.substring(0, clientIpIndex);
			}

			// 預防injection
			if (!Validator.isIpAddress(value)) {
				throw new IllegalArgumentException("illegal IP:" + value);
			}

			return value;
		}
		return httpRequest.getRemoteAddr();
	}

	public String getRootDomain(HttpServletRequest httpRequest) {
		return Optional.ofNullable(httpRequest.getServerName()).orElseGet(() -> StringUtils.EMPTY);
	}
}