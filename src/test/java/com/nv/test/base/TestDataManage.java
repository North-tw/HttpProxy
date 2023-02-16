package com.nv.test.base;

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
import com.nv.manager.SystemInfo;
import com.nv.test.util.ContextUtils;
import com.nv.util.JSONUtils;

public class TestDataManage {
	
	public static void create() {
		beforeClass();
		TestDataManage dataManager = new TestDataManage();
		dataManager.deleteProperties();
		dataManager.createProperties();
		afterClass();
	}
	
	public static void delete() {
		beforeClass();
		new TestDataManage().deleteProperties();
		afterClass();
	}

	@BeforeClass
	public static void beforeClass() {

		ServletContext context = ServletContextMocker.getMocker()
			.addInitParam("ServerID", ContextUtils.getInitParameter("ServerID"))
			.addInitParam("SiteName", ContextUtils.getInitParameter("SiteName"))
			.addInitParam("ServerType", ContextUtils.getInitParameter("ServerType")).mock();

		SystemInfo.getInstance().init(context);
		RedisManager.init();
	}

	@Test
	public void createProperties() {
		DealerProxyProperties dealerProxy = new DealerProxyProperties();
		dealerProxy.setEnableWebserver(1);
		dealerProxy.setPrimaryIp("127.0.0.1");
		dealerProxy.setSecondaryIp("0.0.0.0");
		dealerProxy.setUnlockforwinner(true);
		String dealerProxyJSON = JSONUtils.toJsonStr(dealerProxy);
		RedisManager.get().getBucket(DealerProxyPropertiesDAO.BASE_CACHE_NAME).set(dealerProxyJSON);

		DealerServerProperties dealerServerProperties = new DealerServerProperties();
		dealerServerProperties.setMaxGameRound(100);
		dealerServerProperties.setTableId(998);

		String dealerServerJSON = JSONUtils.toJsonStr(dealerServerProperties);
		RedisManager.get().getMap(DealerServerPropertiesDAO.BASE_CACHE_NAME)
			.put(Integer.valueOf(dealerServerProperties.getTableId()).toString(), dealerServerJSON);

		ResultServerProperties resultServerProperties = new ResultServerProperties();
		resultServerProperties.setDomainCode(this.getClass().getSimpleName());
		resultServerProperties.setResultServerCount(1);
		resultServerProperties.setResultServer(new String[] {"http://127.0.0.1:8090"});
		resultServerProperties.setHttpConnectTimeout(1000);
		String resultServerJSON = JSONUtils.toJsonStr(resultServerProperties);
		RedisManager.get().getBucket(ResultServerPropertiesDAO.BASE_CACHE_NAME).set(resultServerJSON);
	}

	@Test
	public void deleteProperties() {
		RedisManager.get().getBucket(DealerProxyPropertiesDAO.BASE_CACHE_NAME).delete();

		DealerServerProperties dealerServerProperties = new DealerServerProperties();
		dealerServerProperties.setTableId(998);
		RedisManager.get().getMap(DealerServerPropertiesDAO.BASE_CACHE_NAME)
			.remove(Integer.valueOf(dealerServerProperties.getTableId()).toString());

		RedisManager.get().getBucket(ResultServerPropertiesDAO.BASE_CACHE_NAME).delete();
	}

	@AfterClass
	public static void afterClass() {
		RedisManager.shutdown();
	}
}
