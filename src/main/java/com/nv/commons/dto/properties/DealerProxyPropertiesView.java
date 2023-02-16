package com.nv.commons.dto.properties;

public class DealerProxyPropertiesView {

	int httpTimeout;
	int enableWebserver;
	int additionalBetTime;
	int additionalInsuranceBetTime;
	long unlockpending;
	long sendDataPending;
	long sendWinnerDataPending;
	boolean unlockforwinner;
	boolean enableHTTPHeaderConnectionClose;
	boolean enableDealCardConfirm;
	String primaryIp;
	String secondaryIp;
	
	public DealerProxyPropertiesView(DealerProxyPropertiesView data) {
		super();
		this.httpTimeout = data.getHttpTimeout();
		this.enableWebserver = data.getEnableWebserver();
		this.additionalBetTime = data.getAdditionalBetTime();
		this.additionalInsuranceBetTime = data.getAdditionalInsuranceBetTime();
		this.unlockpending = data.getUnlockpending();
		this.sendDataPending = data.getSendDataPending();
		this.sendWinnerDataPending = data.getSendWinnerDataPending();
		this.unlockforwinner = data.isUnlockforwinner();
		this.enableHTTPHeaderConnectionClose = data.isEnableHTTPHeaderConnectionClose();
		this.enableDealCardConfirm = data.isEnableDealCardConfirm();
		this.primaryIp = data.getPrimaryIp();
		this.secondaryIp = data.getSecondaryIp();
	}

	public DealerProxyPropertiesView() {
		super();
	}

	public int getHttpTimeout() {
		return httpTimeout;
	}

	public int getEnableWebserver() {
		return enableWebserver;
	}

	public int getAdditionalBetTime() {
		return additionalBetTime;
	}

	public int getAdditionalInsuranceBetTime() {
		return additionalInsuranceBetTime;
	}

	public long getUnlockpending() {
		return unlockpending;
	}

	public long getSendDataPending() {
		return sendDataPending;
	}

	public long getSendWinnerDataPending() {
		return sendWinnerDataPending;
	}

	public boolean isUnlockforwinner() {
		return unlockforwinner;
	}

	public boolean isEnableHTTPHeaderConnectionClose() {
		return enableHTTPHeaderConnectionClose;
	}

	public boolean isEnableDealCardConfirm() {
		return enableDealCardConfirm;
	}

	public String getPrimaryIp() {
		return primaryIp;
	}

	public String getSecondaryIp() {
		return secondaryIp;
	}

}
