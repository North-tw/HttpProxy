package com.nv.commons.dto.properties;

public class DealerServerProperties extends DealerServerPropertiesView {

	public DealerServerProperties() {
		super();
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setMaxGameRound(int maxGameRound) {
		this.maxGameRound = maxGameRound;
	}

	public void setMaxGameShoe(int maxGameShoe) {
		this.maxGameShoe = maxGameShoe;
	}

	public void setDealerServerType(int dealerServerType) {
		this.dealerServerType = dealerServerType;
	}
}
