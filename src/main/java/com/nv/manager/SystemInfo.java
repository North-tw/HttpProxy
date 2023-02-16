package com.nv.manager;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.nv.commons.type.ServerInfoType;
import com.nv.commons.type.SiteNameType;

public class SystemInfo {

	private static final SystemInfo instance = new SystemInfo();

	private Properties initParameters = new Properties();

	private SiteNameType siteNameType;
	private String serverID;

	// Proxy(1),Manager(4)
	private int serverType;

	// 3 mins
	public static final long keepEventLimitTime = 3 * 60 * 1000;

	public final static SystemInfo getInstance() {
		return instance;
	}

	public void init(ServletContext servletContext) {
		Enumeration<String> initParameterNames = servletContext.getInitParameterNames();

		while (initParameterNames.hasMoreElements()) {
			String paramName = initParameterNames.nextElement();
			initParameters.put(paramName, servletContext.getInitParameter(paramName));
		}

		this.serverID = initParameters.getProperty("ServerID");
		this.serverType = Integer.parseInt(initParameters.getProperty("ServerType"));;
		this.siteNameType = SiteNameType.getInstanceOf(initParameters.getProperty("SiteName"))
			.orElseThrow(() -> new IllegalArgumentException("SiteNameType not found!!!"));

	}

	public SiteNameType getSiteNameType() {
		return this.siteNameType;
	}

	public int getServerType() {
		return serverType;
	}

	public String getServerID() {
		return serverID;
	}

	public String getInitParameter(String paramName) {
		return initParameters.getProperty(paramName);
	}

	public static boolean isManagerServer() {
		int type = SystemInfo.getInstance().getServerType();
		return ((type & ServerInfoType.MANAGER.unique()) > 0);
	}

	public static boolean isProxyServer() {
		int type = SystemInfo.getInstance().getServerType();
		return ((type & ServerInfoType.PROXY_SERVER.unique()) > 0);
	}

}
