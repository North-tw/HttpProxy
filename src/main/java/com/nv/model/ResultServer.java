package com.nv.model;

import java.net.InetAddress;

import com.nv.commons.dto.GameEvent;

public class ResultServer {

	private int id = 0;

	// 負責發送game event 訊息
	private GameEventExecutor gameEventExecutor = null;

	private InetAddress[] address = new InetAddress[2];

	private String[] urls = null;

	private boolean mainServer = false;

	public ResultServer(int id, boolean mainServer) {
		this.id = id;
		this.mainServer = mainServer;
	}

	public void addEvent(GameEvent event) {
		gameEventExecutor.addEvent(event);
	}

	public int getId() {
		return this.id;
	}

	public InetAddress[] getAddress() {
		return address;
	}

	public InetAddress getPrimaryAddress() {
		return address[0];
	}

	public InetAddress getSecondaryAddress() {
		return address[1];
	}

	public String[] getUrls() {
		return urls;
	}

	public void init(String rawUrls, InetAddress primaryAddress, InetAddress secondaryAddress) {

		this.address[0] = primaryAddress;
		this.address[1] = secondaryAddress;

		if (rawUrls != null && !rawUrls.isEmpty()) {
			// TODO 之後可以驗證是否為真正的url網址
			this.urls = rawUrls.split(";");
		}
		this.gameEventExecutor = new GameEventExecutor(this);
	}

	public boolean isMainServer() {
		return mainServer;
	}

}
