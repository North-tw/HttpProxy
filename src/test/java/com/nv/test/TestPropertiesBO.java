package com.nv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Optional;

import javax.servlet.ServletContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nv.commons.dao.redis.properties.DealerProxyPropertiesDAO;
import com.nv.commons.dao.redis.properties.DealerServerPropertiesDAO;
import com.nv.commons.dao.redis.properties.ResultServerPropertiesDAO;
import com.nv.commons.dto.properties.DealerProxyProperties;
import com.nv.commons.dto.properties.DealerServerProperties;
import com.nv.commons.dto.properties.ResultServerProperties;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.model.properties.DealerProxyPropertiesBO;
import com.nv.commons.model.properties.DealerServerPropertiesBO;
import com.nv.commons.model.properties.ResultServerPropertiesBO;
import com.nv.manager.SystemInfo;
import com.nv.test.base.ServletContextMocker;
import com.nv.test.util.ContextUtils;
import com.nv.util.JSONUtils;

public class TestPropertiesBO {

	@BeforeClass
	public static void init() {

		ServletContext context = ServletContextMocker.getMocker()
			.addInitParam("ServerID", ContextUtils.getInitParameter("ServerID"))
			.addInitParam("SiteName", ContextUtils.getInitParameter("SiteName"))
			.addInitParam("ServerType", ContextUtils.getInitParameter("ServerType")).mock();

		SystemInfo.getInstance().init(context);
		RedisManager.init();
	}

	@AfterClass
	public static void shutdown() {
		RedisManager.shutdown();;
	}

	@Test
	public void testDealerProxy() {

		DealerProxyProperties testData = new DealerProxyProperties();
		testData.setHttpTimeout(10000);
		testData.setUnlockpending(1000);
		testData.setEnableWebserver(0);
		testData.setUnlockforwinner(true);
		testData.setSendDataPending(0);
		testData.setSendWinnerDataPending(0);
		testData.setEnableDealCardConfirm(true);
		testData.setAdditionalBetTime(0);
		testData.setAdditionalInsuranceBetTime(0);
		testData.setEnableHTTPHeaderConnectionClose(false);
		testData.setPrimaryIp("111.111.111.111");
		testData.setSecondaryIp("111.111.111.111");

		try {
			String testDataString = JSONUtils.toJsonStr(testData);
			RedisManager.get().getBucket(DealerProxyPropertiesDAO.BASE_CACHE_NAME).set(testDataString);

			Optional<DealerProxyProperties> queryResult = DealerProxyPropertiesBO.get();
			if (queryResult.isEmpty()) {
				fail("testDealerProxy is empty");
			}

			assertEquals(testDataString, JSONUtils.toJsonStr(queryResult.get()));
		} finally {
			RedisManager.get().getBucket(DealerProxyPropertiesDAO.BASE_CACHE_NAME).delete();
		}
	}

	@Test
	public void testDealerServer() {

		DealerServerProperties testData = new DealerServerProperties();
		testData.setTableId(998);
		testData.setLogin("default");
		testData.setUrl("http://127.0.0.1:8080");

		try {
			String testDataString = JSONUtils.toJsonStr(testData);
			RedisManager.get().getMap(DealerServerPropertiesDAO.BASE_CACHE_NAME)
				.put(Integer.valueOf(testData.getTableId()).toString(), testDataString);

			Optional<DealerServerProperties> queryResult = DealerServerPropertiesBO
				.get(testData.getTableId());
			if (queryResult.isEmpty()) {
				fail("testDealerServer is empty");
			}

			assertEquals(testDataString, JSONUtils.toJsonStr(queryResult.get()));
		} finally {
			RedisManager.get().getMap(DealerServerPropertiesDAO.BASE_CACHE_NAME)
				.remove(Integer.valueOf(testData.getTableId()).toString());
		}
	}

	@Test
	public void testResultServer() {

		ResultServerProperties testData = new ResultServerProperties();
		testData.setHttpConnectTimeout(1000);
		testData.setResultServerCount(2);
		testData.setResultServer(new String[]{"http://127.0.0.1:8080", "http://127.0.0.1:8081"});
		testData.setDomainCode("default");
		testData.setProxyHost("http://127.0.0.1");
		testData.setProxyPort(8082);
		testData.setProxyUser("defaultUser");
		testData.setProxyPassword("defaultPassword");

		try {
			String testDataString = JSONUtils.toJsonStr(testData);
			RedisManager.get().getBucket(ResultServerPropertiesDAO.BASE_CACHE_NAME).set(testDataString);

			Optional<ResultServerProperties> queryResult = ResultServerPropertiesBO.get();
			if (queryResult.isEmpty()) {
				fail("testResultServer is empty");
			}

			assertEquals(testDataString, JSONUtils.toJsonStr(queryResult.get()));
		} finally {
			RedisManager.get().getBucket(DealerProxyPropertiesDAO.BASE_CACHE_NAME).delete();
		}
	}
}
