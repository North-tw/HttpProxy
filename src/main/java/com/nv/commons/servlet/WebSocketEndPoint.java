package com.nv.commons.servlet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.nv.commons.dto.Record;
import com.nv.commons.type.SessionRemoveReasonType;
import com.nv.commons.type.WebSocketMessageType;
import com.nv.commons.type.WebSocketRequestType;
import com.nv.expandUtil.util.JSONUtils;
import com.nv.manager.WebSocketSessionManager;
import com.nv.manager.element.MessageElement;
import com.nv.util.LogUtils;

@ServerEndpoint(value = "/jk17y/")
public class WebSocketEndPoint {

	@JsonTypeName("heartbeat")
	private static class HeartbeatRequest extends Request<HeartbeatRequest.Data> {

		private static final class Data {

		}

		private Data data;

		public HeartbeatRequest() {
			super(WebSocketRequestType.heartbeat);
		}

		@Override
		public Data getData() {
			return data;
		}
	}

	@JsonTypeName("record")
	private static class RecordRequest extends Request<RecordRequest.Data> {

		private static final class Data {

			private Record record;

			public Record getRecord() {
				return record;
			}
		}

		private Data data;

		public RecordRequest() {
			super(WebSocketRequestType.record);
		}

		@Override
		public Data getData() {
			return data;
		}
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "router")
	@JsonSubTypes({@JsonSubTypes.Type(value = HeartbeatRequest.class, name = "heartbeat"),
		@JsonSubTypes.Type(value = RecordRequest.class, name = "record")})
	private static abstract class Request<T> {

		protected WebSocketRequestType webSocketRequest;

		public Request(WebSocketRequestType webSocketRequest) {
			super();

			this.webSocketRequest = webSocketRequest;
		}

		public abstract T getData();

		public WebSocketRequestType getWebSocketRequest() {
			return webSocketRequest;
		}
	}

	private void addHeartbeat(Session session) {
		WebSocketSessionManager.getInstance().addHeartbeat(session);
	}

	private void addRecord(Session session, RecordRequest request) {
		System.out.println(session.getId() + " >> " + JSONUtils.toJsonStr(request.getData().getRecord()));
	}

	@OnOpen
	public void init(Session session) {
		try {
			// 提供網路驗證webSocket是否正常連線
			session.getBasicRemote().sendText("{\"status\":\"200\",\"message\":\"connecting\"}");

			WebSocketSessionManager.getInstance().update(session);

			LogUtils.webSocketEndPoint.info("init session, sessionid={}, size={}, user={}", session.getId(),
				WebSocketSessionManager.getInstance().getSize());

			System.out.println("id=" + session.getId());

		} catch (Exception e) {
			LogUtils.webSocketEndPoint.error("sessionId={}, message={}, exception={}", session.getId(),
				e.getMessage(), e);
			try {
				WebSocketSessionManager.getInstance().remove(session, SessionRemoveReasonType.InitException);
			} catch (Exception ie) {
				LogUtils.err.error(ie.getMessage(), ie);
			}

		}
	}

	@OnClose
	public void onClose(Session session) {
		try {
			WebSocketSessionManager.getInstance().remove(session, SessionRemoveReasonType.Close);
			LogUtils.webSocketEndPoint.info("session closed, id={}, size={}, type={}", session.getId(),
				WebSocketSessionManager.getInstance().getSize(), SessionRemoveReasonType.Close.name());
		} catch (Exception e) {
			LogUtils.err.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		try {
			WebSocketSessionManager.getInstance().remove(session, SessionRemoveReasonType.Error);
			LogUtils.webSocketEndPoint.error("error, sessionId={}, size={}, msg={}", session.getId(),
				WebSocketSessionManager.getInstance().getSize(), error.getMessage());
		} catch (Exception e) {
			LogUtils.err.error(e.getMessage(), e);
		}
	}

	/**
	 * invoke onMessage when receiving message from client
	 *
	 * @param message
	 *            message from client
	 * @param session
	 *
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		try {
			LogUtils.webSocketEndPoint.info(message);
			if (!session.isOpen()) {
				LogUtils.webSocketEndPoint.error("session closed: " + message);
				return;
			}

			Request<?> request = JSONUtils.jsonToInstance(message, Request.class);

			switch (request.getWebSocketRequest()) {
				case heartbeat :
					addHeartbeat(session);
					return;
				case record :
					addRecord(session, (RecordRequest) request);
					return;
				default :
					throw new IllegalArgumentException("no such request!!! message:" + message);
			}
		} catch (Exception e) {
			LogUtils.webSocketEndPoint.error("message={}, exception={}", e.getMessage(), e);
			WebSocketSessionManager.getInstance().addMessage(session,
				new MessageElement(WebSocketMessageType.Error,
					JSONUtils.getJSONStr("status", "500", "message", e.getMessage()),
					System.currentTimeMillis()));
		}
	}
}
