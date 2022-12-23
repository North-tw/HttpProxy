package com.nv.commons.dto.properties;

public class DealerServerProperties {

	private int tableid;
	private String url;
	private String login;

	public DealerServerProperties() {
		super();
	}

	public int getTableid() {
		return tableid;
	}

	public void setTableid(int tableid) {
		this.tableid = tableid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
