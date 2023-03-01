package com.nv.test.base;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class SessionMocker {

	private static ConcurrentHashMap<String, SessionMocker> sessionMockers = new ConcurrentHashMap<>();

	private HttpSession session = Mockito.mock(HttpSession.class);
	private Map<String, Object> sessionAttributes = new HashMap<>();
	private String sessionId;

	private SessionMocker(String sessionId) {
		this.sessionId = sessionId;
		when(session.getId()).thenReturn(sessionId);
		Mockito.doAnswer((Answer<Object>) aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			Object value = aInvocation.getArguments()[1];
			sessionAttributes.put(key, value);
			return null;
		}).when(session).setAttribute(anyString(), anyObject());

		when(session.getAttribute(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			return sessionAttributes.get(key);
		});
	}

	public static SessionMocker getInstance(String sessionId) {
		SessionMocker sessionMocker = sessionMockers.computeIfAbsent(sessionId, s -> new SessionMocker(sessionId));
		return sessionMocker;
	}

	public static SessionMocker getInstance() {
		String sessionId = UUID.randomUUID().toString();
		return getInstance(sessionId);
	}

	public void setAttribute(String key, Object value) {
		this.sessionAttributes.put(key, value);
	}

	public void setAttributes(Map<String, Object> sessionAttributes) {
		this.sessionAttributes.putAll(sessionAttributes);
	}

	public HttpSession getHttpSession() {
		return this.session;
	}

	public String getSessionId() {
		return this.sessionId;
	}
}
