package com.nv.commons.dto.properties;


public class DealerProxyProperties {
	
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
	
	public DealerProxyProperties() {
		super();
	}

	public int getHttpTimeout() {
		return httpTimeout;
	}
	
	public void setHttpTimeout(int httpTimeout) {
		this.httpTimeout = httpTimeout;
	}
	
	public int getEnableWebserver() {
		return enableWebserver;
	}
	
	public void setEnableWebserver(int enableWebserver) {
		this.enableWebserver = enableWebserver;
	}
	
	public int getAdditionalBetTime() {
		return additionalBetTime;
	}
	
	public void setAdditionalBetTime(int additionalBetTime) {
		this.additionalBetTime = additionalBetTime;
	}
	
	public int getAdditionalInsuranceBetTime() {
		return additionalInsuranceBetTime;
	}
	
	public void setAdditionalInsuranceBetTime(int additionalInsuranceBetTime) {
		this.additionalInsuranceBetTime = additionalInsuranceBetTime;
	}
	
	public long getUnlockpending() {
		return unlockpending;
	}
	
	public void setUnlockpending(long unlockpending) {
		this.unlockpending = unlockpending;
	}
	
	public long getSendDataPending() {
		return sendDataPending;
	}
	
	public void setSendDataPending(long sendDataPending) {
		this.sendDataPending = sendDataPending;
	}
	
	public long getSendWinnerDataPending() {
		return sendWinnerDataPending;
	}
	
	public void setSendWinnerDataPending(long sendWinnerDataPending) {
		this.sendWinnerDataPending = sendWinnerDataPending;
	}
	
	public boolean isUnlockforwinner() {
		return unlockforwinner;
	}
	
	public void setUnlockforwinner(boolean unlockforwinner) {
		this.unlockforwinner = unlockforwinner;
	}
	
	public boolean isEnableHTTPHeaderConnectionClose() {
		return enableHTTPHeaderConnectionClose;
	}
	
	public void setEnableHTTPHeaderConnectionClose(boolean enableHTTPHeaderConnectionClose) {
		this.enableHTTPHeaderConnectionClose = enableHTTPHeaderConnectionClose;
	}
	
	public boolean isEnableDealCardConfirm() {
		return enableDealCardConfirm;
	}
	
	public void setEnableDealCardConfirm(boolean enableDealCardConfirm) {
		this.enableDealCardConfirm = enableDealCardConfirm;
	}
	
	public String getPrimaryIp() {
		return primaryIp;
	}
	
	public void setPrimaryIp(String primaryIp) {
		this.primaryIp = primaryIp;
	}
	
	public String getSecondaryIp() {
		return secondaryIp;
	}
	
	public void setSecondaryIp(String secondaryIp) {
		this.secondaryIp = secondaryIp;
	}
	
	
}
