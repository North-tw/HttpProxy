package com.nv.test.base;

import java.io.File;
public class SystemPropertyMocker {

	private static final SystemPropertyMocker instance = new SystemPropertyMocker();

	private String root = null;

	public static SystemPropertyMocker getInstance() {
		return instance;
	}

	public void init() {
		if (this.root != null) {
			return;
		}
		ClassLoader classLoader = getClass().getClassLoader();
		String root = new File(classLoader.getResource(".").getFile()).getAbsolutePath();
		// 因為沒有實際的tomcat，所以指向到test專案根目錄
		System.setProperty("catalina.home", root);
		System.setProperty("catalina.base", root);
		this.root = root;
	}

	public String getRoot() {
		if (this.root == null) {
			init();
		}
		return this.root;
	}
}
