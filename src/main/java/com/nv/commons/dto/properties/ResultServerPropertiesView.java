package com.nv.commons.dto.properties;

public class ResultServerPropertiesView {

	int httpConnectTimeout;
	int resultServerCount;
	String[] resultServer;
	String domainCode;
	String proxyHost;
	int proxyPort;
	String proxyUser;
	String proxyPassword;

	public ResultServerPropertiesView() {
		super();
	}

	public ResultServerPropertiesView(ResultServerPropertiesView data) {
		super();
		this.httpConnectTimeout = data.getHttpConnectTimeout();
		this.resultServerCount = data.getResultServerCount();
		this.resultServer = data.getResultServer();
		this.domainCode = data.getDomainCode();
		this.proxyHost = data.getProxyHost();
		this.proxyPort = data.getProxyPort();
		this.proxyUser = data.getProxyUser();
		this.proxyPassword = data.getProxyPassword();
	}

	public int getHttpConnectTimeout() {
		return httpConnectTimeout;
	}

	public int getResultServerCount() {
		return resultServerCount;
	}

	public String[] getResultServer() {
		return resultServer;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

}
