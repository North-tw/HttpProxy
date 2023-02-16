package com.nv.manager;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.nv.commons.dto.GameEvent;
import com.nv.commons.dto.properties.DealerProxyPropertiesView;
import com.nv.commons.dto.properties.ResultServerPropertiesView;
import com.nv.manager.cache.properties.DealerProxyCache;
import com.nv.manager.cache.properties.ResultServerCache;
import com.nv.model.ResultServer;
import com.nv.util.HttpUtil;
import com.nv.util.LogUtils;

/**
 * 負責將event傳送給各result server
 */
public class ResultServerManager {

	private static final ResultServerManager instance = new ResultServerManager();

	private List<ResultServer> resultServers = new ArrayList<>();

	public static ResultServerManager getInstance() {
		return instance;
	}

	private ResultServerManager() {
		super();
	}

	public void init() {
		ResultServerPropertiesView resultServerProperties = ResultServerCache.getInstance().get();
		DealerProxyPropertiesView dealerProxyProperties = DealerProxyCache.getInstance().get();
		InetAddress primaryAddress = HttpUtil.findInetAddress(dealerProxyProperties.getPrimaryIp());
		InetAddress secondaryAddress = HttpUtil.findInetAddress(dealerProxyProperties.getSecondaryIp());
		for (int i = 1; i <= resultServerProperties.getResultServerCount(); i++) {
			ResultServer resultServer = new ResultServer(i, (i == 1) ? true : false);
			resultServer.init(resultServerProperties.getResultServer()[i - 1], primaryAddress,
				secondaryAddress);
			resultServers.add(resultServer);
		}
	}

	/**
	 * 新增遊戲事件，由本 Service 排程送至 ResultServer.
	 *
	 * @param eventType
	 * @param message
	 */
	public synchronized void addEvent(GameEvent gameEvent) {
		LogUtils.gameInfoLogger
			.info("TABLE " + gameEvent.getEventType().name() + " - " + gameEvent.getMessage());
		for (ResultServer resultServer : resultServers) {
			resultServer.addEvent(gameEvent);
		}
	}
}
