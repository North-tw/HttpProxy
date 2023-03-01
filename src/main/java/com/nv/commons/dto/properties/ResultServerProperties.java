package com.nv.commons.dto.properties;

public class ResultServerProperties extends ResultServerPropertiesView{

	public ResultServerProperties() {
		super();
	}

	public void setHttpConnectTimeout(int httpConnectTimeout) {
		this.httpConnectTimeout = httpConnectTimeout;
	}

	public void setResultServerCount(int resultServerCount) {
		this.resultServerCount = resultServerCount;
	}

	public void setResultServer(String[] resultServer) {
		this.resultServer = resultServer;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

}
