package com.nv.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.dealerserver.Message;
import com.nv.servlet.servlet.ProxyServlet;
import com.nv.test.base.RequestMocker;
import com.nv.test.base.ResultServerMocker;
import com.nv.test.base.TestDataManage;
import com.nv.test.base.TomcatMocker;
import com.nv.test.dto.TestBody;
import com.nv.test.util.SingleTestUtils;
import com.nv.util.EncryptUtils;
import com.nv.util.JSONUtils;
import com.nv.util.LogUtils;

public class TestProxy {

	@BeforeClass
	public static void beforeClass() {
		TestDataManage.create();
		TomcatMocker.getInstance().contextInitialized();
		ResultServerMocker.getInstance().start();
	}

	@Test
	public void fullTest() throws Exception {

		TestBody testBody = new TestBody();
		testBody.setDealerId("1");
		testBody.setGameRound(2);
		testBody.setGameShoe(3);
		testBody.setTableID(998);

		Message message = new Message();
		message.setEventType(999);
		message.setBody(JSONUtils.toJsonStr(testBody));

		String messageJSON = JSONUtils.toJsonStr(message);

		RequestMocker requestMocker = new RequestMocker(ProxyServlet.class, "/");
		requestMocker.setParameter("message",
			EncryptUtils.desedeEncoder(messageJSON, SystemConstant.DEALER_EVENT_AES_TOKEN));

		LogUtils.system.info("{} response = {}", ProxyServlet.class.getSimpleName(), requestMocker.execute());
		LogUtils.system.info("ProxyServlet before encoder = {}", SingleTestUtils.getUtil().getIn());
		LogUtils.system.info("ResultServerMocker after decoder = {}", SingleTestUtils.getUtil().getOut());

		assertEquals(SingleTestUtils.getUtil().getIn(), SingleTestUtils.getUtil().getOut());

	}
	
	@After
	public void after() {
		SingleTestUtils.getUtil().clear();
	}

	@AfterClass
	public static void afterClass() {
		ResultServerMocker.getInstance().shutdown();;
		TomcatMocker.getInstance().contextDestroyed();
		TestDataManage.delete();
	}
}
