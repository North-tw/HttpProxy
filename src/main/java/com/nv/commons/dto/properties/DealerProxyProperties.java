package com.nv.commons.dto.properties;

public class DealerProxyProperties extends DealerProxyPropertiesView {

	public DealerProxyProperties() {
		super();
	}

	public void setHttpTimeout(int httpTimeout) {
		this.httpTimeout = httpTimeout;
	}

	public void setEnableWebserver(int enableWebserver) {
		this.enableWebserver = enableWebserver;
	}

	public void setAdditionalBetTime(int additionalBetTime) {
		this.additionalBetTime = additionalBetTime;
	}

	public void setAdditionalInsuranceBetTime(int additionalInsuranceBetTime) {
		this.additionalInsuranceBetTime = additionalInsuranceBetTime;
	}

	public void setUnlockpending(long unlockpending) {
		this.unlockpending = unlockpending;
	}

	public void setSendDataPending(long sendDataPending) {
		this.sendDataPending = sendDataPending;
	}

	public void setSendWinnerDataPending(long sendWinnerDataPending) {
		this.sendWinnerDataPending = sendWinnerDataPending;
	}

	public void setUnlockforwinner(boolean unlockforwinner) {
		this.unlockforwinner = unlockforwinner;
	}

	public void setEnableHTTPHeaderConnectionClose(boolean enableHTTPHeaderConnectionClose) {
		this.enableHTTPHeaderConnectionClose = enableHTTPHeaderConnectionClose;
	}

	public void setEnableDealCardConfirm(boolean enableDealCardConfirm) {
		this.enableDealCardConfirm = enableDealCardConfirm;
	}

	public void setPrimaryIp(String primaryIp) {
		this.primaryIp = primaryIp;
	}

	public void setSecondaryIp(String secondaryIp) {
		this.secondaryIp = secondaryIp;
	}

}
