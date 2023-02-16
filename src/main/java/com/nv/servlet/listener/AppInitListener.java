package com.nv.servlet.listener;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.nv.commons.constant.Setting;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.model.database.RedisPubSubManager;
import com.nv.expandUtil.util.ExceptionUtils;
import com.nv.manager.CountryLookup;
import com.nv.manager.FactoryManager;
import com.nv.manager.ResultServerManager;
import com.nv.manager.SystemInfo;
import com.nv.manager.cache.properties.DealerProxyCache;
import com.nv.manager.cache.properties.DealerServerCache;
import com.nv.manager.cache.properties.ResultServerCache;
import com.nv.util.DateUtils;
import com.nv.util.LogUtils;

@WebListener
public class AppInitListener implements ServletContextListener {

	// 程式執行的時區，避免設定錯誤導致不可預期的問題
	private String SERVER_TIME_ZONE = "GMT+8:00";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			// 檢查目前server的時區是否正確
			LogUtils.system.info("validate timezone");
			checkTimeZone();

			LogUtils.system.info("init SystemInfo");
			SystemInfo.getInstance().init(sce.getServletContext());

			LogUtils.system.info("init CountryLookup");
			CountryLookup.getInstance().init();

			// initialize JS File Version. Reset when restarting server.
			Setting.JS_FILE_VERSION = Integer.parseInt(DateUtils.toString(new Date(), "yyyyMMddHH"));

			LogUtils.system.info("init FactoryManager");
			FactoryManager.init();

			LogUtils.system.info("init RedisManager");
			RedisManager.init();

			LogUtils.system.info("init DealerProxyCache");
			DealerProxyCache.getInstance().init();

			LogUtils.system.info("init DealerServerCache");
			DealerServerCache.getInstance().init();

			LogUtils.system.info("init ResultServerCache");
			ResultServerCache.getInstance().init();

			LogUtils.system.info("init ResultServerManager");
			ResultServerManager.getInstance().init();

			LogUtils.system.info("init RedisPubSubManager");
			RedisPubSubManager.init();

			LogUtils.system.info("AppInitListener init finish!");

		} catch (Exception e) {
			LogUtils.system.error("AppInitListener init error!!!", e);
			ExceptionUtils.amendToUncheckedException(e);
		}
	}

	private void checkTimeZone() {
		String serverTimeZone = displayTimeZone(TimeZone.getDefault());
		if (!SERVER_TIME_ZONE.equals(serverTimeZone)) {
			throw new RuntimeException(
				"Server Time Zone Error !! : " + serverTimeZone + " != " + SERVER_TIME_ZONE);
		}
	}

	private String displayTimeZone(TimeZone timeZone) {
		long hours = TimeUnit.MILLISECONDS.toHours(timeZone.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeZone.getRawOffset())
			- TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		if (hours > 0) {
			return String.format("GMT+%d:%02d", hours, minutes);
		} else {
			return String.format("GMT%d:%02d", hours, minutes);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		/*
		 * RedisManager.shutdown(); RedisPubSubManager.shutdown();
		 * 以上兩個方法順序必須在Redis各項服務之後執行!!
		 */
		try {
			LogUtils.system.info("Shutdown All RedisCluster connections");
			RedisManager.shutdown();
		} catch (Exception e) {
			LogUtils.system.error("Shutdown All RedisCluster connections Error", e);
		}
		try {
			LogUtils.system.info("Shutdown All RedisSentinel connections");
			RedisPubSubManager.shutdown();
		} catch (Exception e) {
			LogUtils.system.error("Shutdown All RedisSentinel connections Error", e);
		}
	}
}
