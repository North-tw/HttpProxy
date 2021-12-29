package com.nv.manager;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.websocket.Session;

import com.nv.commons.type.SessionRemoveReasonType;
import com.nv.commons.type.WebSocketMessageType;
import com.nv.commons.type.WebSocketRequestType;
import com.nv.expandUtil.exception.ElementNotFoundException;
import com.nv.expandUtil.util.JSONUtils;
import com.nv.manager.element.MessageElement;
import com.nv.manager.element.WebSocketElement;
import com.nv.util.LogUtils;
import com.nv.util.WebSocketUtils;

/**
 * 管理 web socket session 主動發送資料、關閉連線
 */
public class WebSocketSessionManager {

	private static final WebSocketSessionManager instance = new WebSocketSessionManager();

	private static final ConcurrentMap<String, WebSocketElement> cache = new ConcurrentHashMap<>();

	private final String heartbeatResponse = "{\"status\":\"200\",\"router\":\"heartbeat\",\"data\":{}}";

	// 預計5000個玩家在同一台Server，預備500個Thread因應，會因為同時存取直接提升並自行消滅
	private ScheduledExecutorService exec = Executors.newScheduledThreadPool(500);

	// millisecond websocket break seconds
	private final long breakTime = 30 * 1000;

	private WebSocketSessionManager() {
		super();
	}

	public static final WebSocketSessionManager getInstance() {
		return instance;
	}

	public Optional<WebSocketElement> get(String sessionId) {
		return Optional.ofNullable(cache.get(sessionId));
	}

	public void remove(String sessionId) {
		WebSocketElement element = cache.remove(sessionId);

		if (element != null) {
			element.shutdown();
			LogUtils.webSocket.debug("local-remove sessionId={}", sessionId);
		} else {
			LogUtils.webSocket.debug("local-remove sessionId={} failed", sessionId);
		}
	}

	public void remove(Session session, SessionRemoveReasonType reasonType) {
		String id = session.getId();

		// 移除本機 cache
		LogUtils.webSocket.debug("local-remove sessionId={}, reason={}", id, reasonType.name());
		cache.entrySet().removeIf(entry -> {
			WebSocketElement element = entry.getValue();

			if (element.getSessionId().equals(id)) {
				element.shutdown();
				LogUtils.webSocket.debug("local-remove session, sessionId={}, reason={}", id,
					reasonType.name());
				return true;
			}
			return false;
		});

		// 不管如何關閉連線
		WebSocketUtils.close(session);
		LogUtils.webSocket.debug("local-remove session closed, sessionId={}, reason={} ", id,
			reasonType.name());
	}

	public void shutdownAll() {
		try {
			cache.entrySet().forEach((obj) -> obj.getValue().shutdown());
		} catch (Exception e) {
			LogUtils.err.error("WebSocketSessionManager entry shutdown error!!!", e);
		}
		exec.shutdown();
	}

	// 登入成功，建立連線後
	public void update(Session session) {
		// update local cache
		LogUtils.webSocket.info("local-add session for sessionId={}", session.getId());
		Optional
			.ofNullable(
				cache.put(session.getId(), new WebSocketElement(session, WebSocketRequestType.record, exec)))
			.ifPresent(element -> {
				element.shutdown();
				LogUtils.webSocket.info("local-replace session for sessionId={}", session.getId());
			});
	}

	public void addHeartbeat(Session session) throws ElementNotFoundException {

		MessageElement message = new MessageElement(WebSocketMessageType.Heartbeat, heartbeatResponse,
			System.currentTimeMillis());

		cache.entrySet().stream().filter(obj -> obj.getValue().getSessionId().equals(session.getId()))
			.findFirst().ifPresent((a) -> {
				LogUtils.webSocket.info("addHeartBeat - sessionid:" + session.getId() + ", MessageElement:"
					+ JSONUtils.toJsonStr(a.getValue()));
				a.getValue().setHeartbeatTimeStamp(System.currentTimeMillis());
				a.getValue().putMessageToQueue(message);
			});

	}

	public void addMessage(Session session, MessageElement message) throws ElementNotFoundException {
		cache.entrySet().stream().filter(obj -> obj.getValue().getSessionId().equals(session.getId()))
			.findFirst().ifPresent((a) -> {
				a.getValue().putMessageToQueue(message);
			});
	}

	public void sendMessage(String sessionId, WebSocketMessageType type, String message) {
		cache.entrySet().stream().filter(obj -> obj.getKey().equals(sessionId)).findFirst().ifPresent((a) -> {
			MessageElement messageElement = new MessageElement(type, message, System.currentTimeMillis());
			a.getValue().putMessageToQueue(messageElement);
		});
	}

	public int getSize() {
		return cache.size();
	}

	public void breakUnusedSession() {
		long now = System.currentTimeMillis();
		cache.entrySet().stream().filter(obj -> (obj.getValue().getHeartbeatTimeStamp() + breakTime) < now)
			.forEach(obj -> remove(obj.getValue().getSessionId()));
	}
}
