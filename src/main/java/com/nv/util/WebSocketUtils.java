package com.nv.util;

import javax.websocket.Session;

public class WebSocketUtils {

	/**
	 * 發生任何例外都無法處理
	 */
	public static void close(Session session) {
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (Exception ex) {
			// nothing needs to be done
		}
	}

	private WebSocketUtils() {
		throw new AssertionError();
	}
}