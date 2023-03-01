package com.nv.commons.type;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

import com.nv.commons.listener.IEventListener;

/**
 * Only use for dealer connection.
 *
 * @author SONY
 * 
 */
public enum GameEventType implements IEventListener {

	GP_INIT(1, null), GP_NEW_GAME_START(2, null), GP_BET_TIME(3, null), GP_CHANGE_STATE(4,
		null), GP_ONE_CARD_DRAWN(6, null), GP_WINNER(7, null), GP_LOCKUP(9, null), GP_DEAL_CARD_CONFIRM(10,
			null), GP_SKIP_ROUND(11, null), GP_BET_ONEDAY(12, null), GP_RESHAKE_SICBO(13,
				null), GP_HEARTBEAT(14, null), GP_RANDOM_PAY(15,
					null), GP_DEALERID(24, null), GP_VOID_ROUND(25, null), GP_BET_INSURANCE(26, null),
	// 與 Table 斷線時產生.
	GP_DISCONNECT(701, null);

	private final static GameEventType[] VALUES;

	static {
		int maxCode = Arrays.stream(GameEventType.values()).mapToInt(e -> e.code).max().getAsInt();

		VALUES = new GameEventType[maxCode + 1];
		for (GameEventType e : GameEventType.values()) {
			VALUES[e.code] = e;
		}
	}

	private final int code;

	private IEventListener eventListener;

	private GameEventType(int code, IEventListener eventListener) {
		this.code = code;
		this.eventListener = eventListener;
	}

	public int getCode() {
		return code;
	}

	public static Optional<GameEventType> getInstanceOf(int code) {
		if (ArrayUtils.isArrayIndexValid(VALUES, code)) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(VALUES[code]);
		}
	}

	public IEventListener getEventListener() {
		return this.eventListener;
	}

	public void afterEventProcessed() {

	}

	public int getRetryTimes() {
		return 3;
	}

	@Override
	public void handleEvent(String message) throws Exception {
		eventListener.handleEvent(message);
	}

	@Override
	public GameEventType getGameEvent() {
		return this;
	}

}
