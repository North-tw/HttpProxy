package com.nv.commons.dto.dealerserver;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Message {

	private int tableID;
	private int domain;
	private int eventType;
	private String body;

	public int getTableID() {
		return tableID;
	}

	public void setTableID(int tableID) {
		this.tableID = tableID;
	}

	public int getDomain() {
		return domain;
	}

	public void setDomain(int domain) {
		this.domain = domain;
	}

	@JsonGetter("x1")
	public int getEventType() {
		return eventType;
	}

	@JsonSetter("x1")
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	@JsonGetter("x")
	public String getBody() {
		return body;
	}

	@JsonSetter("x")
	public void setBody(String body) {
		this.body = body;
	}

}
