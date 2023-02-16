package com.nv.commons.dto.result;

public abstract class BaseResultObj {

	// 用來記錄是否已經送出過 BetTime.
	protected boolean betSend;

	// 牌手帳號.
	protected String dealerId = "";

	protected String dealerSerialNo = "";

	/**
	 * 小場次 == iCount.
	 */
	protected int gameRound = -1;

	protected long gameShoe = -1;

	// 遊戲狀態
	protected byte gameState = -1;

	// 倒數時間
	protected int iTime;

	// 鎖定的原因
	protected int lockReason;

	// 經理帳號用於記錄Void Round
	protected String managerId = "";

	protected long roundEndTime = -1;

	protected long roundStartTime = -1;
	
	
	protected String uuId = "";

	
	public String getDealerId() {
		return dealerId;
	}

	public String getDealerSerialNo() {
		return dealerSerialNo;
	}

	public int getGameRound() {
		return gameRound;
	}

	public long getGameShoe() {
		return gameShoe;
	}

	public byte getGameState() {
		return gameState;
	}

	public int getiTime() {
		return iTime;
	}

	public int getLockReason() {
		return lockReason;
	}

	public String getManagerId() {
		return managerId;
	}

	public long getRoundEndTime() {
		return roundEndTime;
	}

	public long getRoundStartTime() {
		return roundStartTime;
	}

	public String getUuId() {
		return uuId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public void setDealerSerialNo(String dealerSerialNo) {
		this.dealerSerialNo = dealerSerialNo;
	}

	public void setGameRound(int gameRound) {
		this.gameRound = gameRound;
	}

	public void setGameShoe(long gameShoe) {
		this.gameShoe = gameShoe;
	}

	public void setGameState(byte gameState) {
		this.gameState = gameState;
	}

	public void setiTime(int iTime) {
		this.iTime = iTime;
	}

	public void setLockReason(int lockReason) {
		this.lockReason = lockReason;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public void setRoundEndTime(long roundEndTime) {
		this.roundEndTime = roundEndTime;
	}

	public void setRoundStartTime(long roundStartTime) {
		this.roundStartTime = roundStartTime;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

}
