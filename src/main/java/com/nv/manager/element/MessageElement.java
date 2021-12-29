package com.nv.manager.element;

import com.nv.commons.type.WebSocketMessageType;

public class MessageElement {

	private WebSocketMessageType type;

	private String message;

	private long messageTime;

	public MessageElement(WebSocketMessageType type, String message, long messageTime) {
		this.type = type;
		this.message = message;
		this.messageTime = messageTime;
	}

	public WebSocketMessageType getType() {
		return type;
	}

	public void setType(WebSocketMessageType type) {
		this.type = type;
	}

	public long getMessageTime() {
		return messageTime;
	}

	public String getMessage() {
		return message;
	}
}
