package com.nv.test.dto;

public class TestBody {

	private int tableID;
	private int gameRound;
	private int gameShoe;
	private String dealerId;

	public TestBody() {
	}

	public int getTableID() {
		return tableID;
	}

	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	public int getGameRound() {
		return gameRound;
	}

	public void setGameRound(int gameRound) {
		this.gameRound = gameRound;
	}

	public int getGameShoe() {
		return gameShoe;
	}

	public void setGameShoe(int gameShoe) {
		this.gameShoe = gameShoe;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

}
