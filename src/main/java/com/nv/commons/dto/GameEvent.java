package com.nv.commons.dto;

import com.nv.commons.type.GameEventType;

/**
 * 代表遊戲事件，用於資料傳遞使用
 * 
 * 
 */
public class GameEvent {

	private GameEventType eventType;
	
	private int tableId;

	private long gameShoe;

	private int gameRound;

	private long deliverTime;

	private String message;

	public GameEvent(int tableId, long gameShoe, int gameRound, GameEventType eventType, long deliverTime, String message) {
		super();

		this.tableId = tableId;
		this.gameShoe = gameShoe;
		this.gameRound = gameRound;
		this.eventType = eventType;
		this.deliverTime = deliverTime;
		this.message = message;
	}

	public long getDeliverTime() {
		return this.deliverTime;
	}

	public GameEventType getEventType() {
		return eventType;
	}

	public int getTableId() {
		return tableId;
	}

	public int getGameRound() {
		return gameRound;
	}

	public long getGameShoe() {
		return gameShoe;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "GameEvent [eventType=" + eventType + ", gameShoe=" + gameShoe + ", gameRound=" + gameRound
			+ ", deliverTime=" + deliverTime + ", message=" + message + "]";
	}
}
