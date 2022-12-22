package com.nv.commons.dto.properties;

public class ResultServerProperties {

	private int httpConnectTimeout;
	private int resultServerCount;
	private String[] resultServer;
	private String domainCode;
	private String proxyHost;
	private String proxyPort;
	private String proxyUser;
	private String proxyPassword;

	public ResultServerProperties() {
		super();
	}

	public int getHttpConnectTimeout() {
		return httpConnectTimeout;
	}

	public void setHttpConnectTimeout(int httpConnectTimeout) {
		this.httpConnectTimeout = httpConnectTimeout;
	}

	public int getResultServerCount() {
		return resultServerCount;
	}

	public void setResultServerCount(int resultServerCount) {
		this.resultServerCount = resultServerCount;
	}

	public String[] getResultServer() {
		return resultServer;
	}

	public void setResultServer(String[] resultServer) {
		this.resultServer = resultServer;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

}
