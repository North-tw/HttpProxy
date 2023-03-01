package com.nv.commons.dto.properties;

public class DealerServerPropertiesView {

	int tableId;
	String url;
	String login;
	int maxGameRound;
	int maxGameShoe;
	int dealerServerType;

	public DealerServerPropertiesView(DealerServerPropertiesView data) {
		super();
		this.tableId = data.getTableId();
		this.url = data.getUrl();
		this.login = data.getLogin();
		this.maxGameRound = data.getMaxGameRound();
		this.maxGameShoe = data.getMaxGameShoe();
		this.dealerServerType = data.getDealerServerType();
	}

	public DealerServerPropertiesView() {
		super();
	}

	public int getTableId() {
		return tableId;
	}

	public String getUrl() {
		return url;
	}

	public String getLogin() {
		return login;
	}

	public int getMaxGameRound() {
		return maxGameRound;
	}

	public int getMaxGameShoe() {
		return maxGameShoe;
	}

	public int getDealerServerType() {
		return dealerServerType;
	}

}
