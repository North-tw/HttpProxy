package com.nv.manager;

import com.nv.commons.type.SiteNameType;

public class SystemInfo {

	private static final SystemInfo instance = new SystemInfo();

	private SiteNameType siteNameType;
	private String serverID;

	// Proxy(1),Manager(4)
	private int serverType;

	public final static SystemInfo getInstance() {
		return instance;
	}

	public void init(String serverID, int serverType, SiteNameType siteNameType) {
		this.serverID = serverID;
		this.serverType = serverType;
		this.siteNameType = siteNameType;

		// query at init
		this.update();
	}

	public void update() {

	}

	public SiteNameType getSiteNameType() {
		return this.siteNameType;
	}
}
