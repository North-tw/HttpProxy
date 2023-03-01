package com.nv.model;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.GameEvent;
import com.nv.commons.dto.properties.DealerProxyPropertiesView;
import com.nv.commons.dto.properties.DealerServerPropertiesView;
import com.nv.commons.dto.properties.ResultServerPropertiesView;
import com.nv.manager.GlobalThreadPool;
import com.nv.manager.OKHttpClientManager;
import com.nv.manager.OKHttpClientManager.HTTPResponse;
import com.nv.manager.OKHttpClientManager.HttpPostRequest;
import com.nv.manager.SystemInfo;
import com.nv.manager.cache.properties.DealerProxyCache;
import com.nv.manager.cache.properties.DealerServerCache;
import com.nv.manager.cache.properties.ResultServerCache;
import com.nv.util.EventUtils;
import com.nv.util.LogUtils;

/**
 * 和 GameEventService 緊密整合，用來紀錄、執行某台 ResultServer 的遊戲事件.
 *
 */
public class GameEventExecutor {

	private ResultServer resultServer;

	public GameEventExecutor(ResultServer resultServer) {
		this.resultServer = resultServer;
	}

	public void addEvent(GameEvent gameEvent) {
		try {
			long deliverTime = gameEvent.getDeliverTime();

			// 目前時間 - deliverTime 超過3分鐘的事件不送給Server
			if (System.currentTimeMillis() - deliverTime > SystemInfo.keepEventLimitTime) {
				return;
			}
			int maxRetry = gameEvent.getEventType().getRetryTimes();

			int i = 0;

			for (; i < maxRetry; i++) {
				boolean success = sendEventToResultServer(gameEvent);
				if (success) {
					break;
				}
			}

			if (i >= maxRetry) {
				LogUtils.gameErrorLogger
					.error("sendEventToResultServer fail : " + gameEvent.getEventType().name());
			}
		} catch (RuntimeException | InterruptedException ex) {
			LogUtils.gameErrorLogger.error("URGENT CORE Task Processing Error - ", ex);
		}
	}

	private boolean sendEventToResultServer(GameEvent gameEvent) throws InterruptedException {
		boolean success = false;

		DealerProxyPropertiesView dealerProxyProp = DealerProxyCache.getInstance().get();
		DealerServerPropertiesView dealerServerProp = DealerServerCache.getInstance()
			.get(gameEvent.getTableId());
		ResultServerPropertiesView resultServerProp = ResultServerCache.getInstance().get();

		// 不傳送資料到WebServer
		if (dealerProxyProp.getEnableWebserver() != 1) {
			// 強制判斷成功
			LogUtils.gameInfoLogger.info("Status: Disable WebServer!!!");
			success = true;
		} else if (gameEvent.getGameShoe() > 0 && (gameEvent.getGameRound() > 0
			&& gameEvent.getGameRound() <= dealerServerProp.getMaxGameRound())) {
			// shoe爲大於0的正數，以及round爲小於等於maxGameRound的大於0的正數，才傳送資料

			// 有一個回復就繼續執行
			final CountDownLatch latch = new CountDownLatch(1);
			final boolean[] result = new boolean[]{false};
			Arrays.stream(resultServer.getUrls()).parallel().forEach(url -> {
				Arrays.stream(resultServer.getAddress()).parallel().filter(address -> address != null)
					.forEach(address -> {
						GlobalThreadPool.execute(() -> {
							HTTPResponse response = null;
							try {
								HttpPostRequest request = OKHttpClientManager.getInstance()
									.getHttpPostRequest(url,
										dealerProxyProp.isEnableHTTPHeaderConnectionClose());
								request
									.setParameters(EventUtils.createParamsMap(gameEvent.getEventType().name(),
										gameEvent.getMessage(), gameEvent.getTableId()));
								request.setTimeout(resultServerProp.getHttpConnectTimeout());

								if (resultServerProp.getProxyHost() != null) {
									request.setProxy(resultServerProp.getProxyHost(),
										resultServerProp.getProxyPort(), resultServerProp.getProxyUser(),
										resultServerProp.getProxyPassword());
								}
								long requestTime = System.currentTimeMillis();
								response = OKHttpClientManager.getInstance().execute(request);
								String log = String.format(
									"URL[%s] Table[%s] Event[%s] RESPONSE[%s] - ADDRESS[%s] - gameShoe:%sgameRound:%s - HTTPTIME[%s]",
									url, gameEvent.getTableId(), gameEvent.getEventType().name(),
									response.getContent(), address.getHostAddress(), gameEvent.getGameShoe(),
									gameEvent.getGameRound(), System.currentTimeMillis() - requestTime);

								LogUtils.gameInfoLogger.info(log);

							} catch (Exception ex) {
								LogUtils.gameErrorLogger.error(ex.getMessage() + " , URL:" + url + " , Event:"
									+ gameEvent.getEventType().name(), ex);
							} finally {
								// unLockForWinner設定為true時，需檢查statusCode和content
								if (dealerProxyProp.isUnlockforwinner()) {
									// 其中收到一個response為正確回應時，即winnerUnlock解鎖。
									if (response != null
										&& response.getStatusCode() == SystemConstant.SUCCESS_CODE
										&& response.getContent().equals(SystemConstant.SUCCESS)) {
										if (!result[0]) {
											result[0] = true;
											latch.countDown();
										}
									}
								} else {
									// enableUnLockForWinner設定為false時，避免卡在await方法，保證第一個回來的response即解鎖
									if (!result[0]) {
										result[0] = true;
									}
									latch.countDown();
								}
							}
						});
					});
			});
			boolean completed = latch.await(2000, TimeUnit.MILLISECONDS);
			if (!completed) {
				LogUtils.gameInfoLogger.info("GameShoe:" + gameEvent.getGameShoe() + " , GameRound:"
					+ gameEvent.getGameRound() + " , result[0]:" + result[0]
					+ " , due to no current response so relase latch await!!");
			}
			success = result[0];
		} else {
			// 不正確shoe或round，解鎖桌子但是不傳送資料
			LogUtils.gameErrorLogger.error("error shoe or round : " + gameEvent);
			success = true;
		}
		// 傳送成功的後續動作，完全由第1組Executor主控
		if (success && resultServer.isMainServer()) {
			// 傳送成功後是否有需要執行的動作
			gameEvent.getEventType().afterEventProcessed();
		}
		return success;
	}
}
