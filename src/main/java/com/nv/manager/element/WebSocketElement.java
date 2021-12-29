package com.nv.manager.element;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.websocket.Session;

import com.nv.commons.type.WebSocketRequestType;
import com.nv.util.LogUtils;
import com.nv.util.WebSocketUtils;

public class WebSocketElement {

	private BlockingQueue<MessageElement> sendQueue = new ArrayBlockingQueue<MessageElement>(200);

	private Session session;

	private WebSocketRequestType webSocketRequestType;

	private long heartbeatTimeStamp = System.currentTimeMillis();

	private long lastActionTime = System.currentTimeMillis();

	private ScheduledFuture<?> future;

	public WebSocketElement(Session session, WebSocketRequestType webSocketRequestType,
		ScheduledExecutorService exec) {
		super();

		this.session = session;
		this.webSocketRequestType = webSocketRequestType;
		this.future = exec.scheduleWithFixedDelay(() -> {
			try {
				long fireTime = System.currentTimeMillis();
				long delayTime = fireTime - lastActionTime;

				if (delayTime >= 1000) {
					LogUtils.messageQueue.info("timeout:id:" + session.getId() + ", delay time:" + delayTime);
				}
				lastActionTime = fireTime;

				MessageElement message = sendQueue.poll();
				if (message == null) {
					return;
				}
				if (session.isOpen()) {
					long diff = System.currentTimeMillis() - message.getMessageTime();
					if (diff > 3000) {
						LogUtils.messageQueue
							.info("timeout:id:" + session.getId() + "," + message.getMessageTime() + ","
								+ diff + "," + webSocketRequestType.name() + ", type:" + message.getType());
						return;
					}
					session.getBasicRemote().sendText(message.getMessage());

					LogUtils.messageQueue.info("id:" + session.getId() + "," + message.getMessageTime()
						+ ", diff:" + diff + ", size:" + sendQueue.size() + ", " + webSocketRequestType.name()
						+ ", type:" + message.getType());
				} else {
					// 記錄遭遇session未開
					LogUtils.messageQueue
						.error("notOpen:id:" + session.getId() + "," + message.getMessageTime() + ","
							+ webSocketRequestType.name() + ", type:" + message.getType());
				}
			} catch (Exception e) {
				LogUtils.err.info(e.getMessage(), e);
			}
		}, 0, 200, TimeUnit.MILLISECONDS);
	}

	public long getHeartbeatTimeStamp() {
		return heartbeatTimeStamp;
	}

	public String getSessionId() {
		return session.getId();
	}

	public WebSocketRequestType getWebSocketRequestType() {
		return webSocketRequestType;
	}

	public void putMessageToQueue(MessageElement message) {
		sendQueue.add(message);
	}

	public void setHeartbeatTimeStamp(long heartbeatTimeStamp) {
		this.heartbeatTimeStamp = heartbeatTimeStamp;
	}

	public void setWebSocketRequestType(WebSocketRequestType webSocketRequestType) {
		this.webSocketRequestType = webSocketRequestType;
	}

	public void shutdown() {
		try {
			future.cancel(true);
		} catch (Exception e) {
			LogUtils.err.info(e.getMessage(), e);
		}
		try {
			sendQueue.clear();
			WebSocketUtils.close(session);
		} catch (RuntimeException e) {
			LogUtils.err.info(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public String toString() {
		return "WebSocketElement [sendQueue=" + sendQueue + ", session=" + session + ", webSocketRequestType="
			+ webSocketRequestType + "]";
	}
}
