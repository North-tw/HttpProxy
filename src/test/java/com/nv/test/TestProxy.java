package com.nv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.Filter;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nv.commons.constant.Setting;
import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.dealerserver.Message;
import com.nv.servlet.filter.EventFilter;
import com.nv.servlet.servlet.ProxyServlet;
import com.nv.servlet.servlet.SettingServlet;
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
	public void testUpdateSetting() {
		final boolean original = Setting.STOP_RECEIVE_DEALER_EVENT;
		RequestMocker requestMocker = new RequestMocker(SettingServlet.class, "/updateSetting");
		requestMocker.setParameter("key", "STOP_RECEIVE_DEALER_EVENT");
		requestMocker.setParameter("value", Boolean.toString(!original));
		LogUtils.system.info(requestMocker.execute());
		assertNotEquals(original, Setting.STOP_RECEIVE_DEALER_EVENT);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEnableProxy() throws Exception {
		Setting.STOP_RECEIVE_DEALER_EVENT = false;
		Class<? extends Filter>[] filters = ArrayUtils.toArray(EventFilter.class);
		doProxy(ArrayUtils.toArray(r -> r.withFilter(filters)));
		assertNotNull(SingleTestUtils.getDataUtil().getOut());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDisableProxy() throws Exception {
		Setting.STOP_RECEIVE_DEALER_EVENT = true;
		Class<? extends Filter>[] filters = ArrayUtils.toArray(EventFilter.class);
		doProxy(ArrayUtils.toArray(r -> r.withFilter(filters)));
		assertNull(SingleTestUtils.getDataUtil().getOut());
	}

	@Test
	public void testProxy() throws Exception {
		doProxy(null);
		assertEquals(SingleTestUtils.getDataUtil().getIn(), SingleTestUtils.getDataUtil().getOut());
	}

	private void doProxy(Consumer<RequestMocker>[] reqModifier) throws Exception {

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

		Optional.ofNullable(reqModifier).stream().flatMap(a -> Arrays.stream(a))
			.forEach(c -> c.accept(requestMocker));

		requestMocker.setParameter("message",
			EncryptUtils.desedeEncoder(messageJSON, SystemConstant.DEALER_EVENT_AES_TOKEN));

		LogUtils.system.info("{} response = {}", ProxyServlet.class.getSimpleName(), requestMocker.execute());
		LogUtils.system.info("ProxyServlet before encoder = {}", SingleTestUtils.getDataUtil().getIn());
		LogUtils.system.info("ResultServerMocker after decoder = {}", SingleTestUtils.getDataUtil().getOut());
	}

	@After
	public void after() {
		SingleTestUtils.getDataUtil().clear();
		Setting.STOP_RECEIVE_DEALER_EVENT = true;
	}

	@AfterClass
	public static void afterClass() {
		ResultServerMocker.getInstance().shutdown();;
		TomcatMocker.getInstance().contextDestroyed();
		TestDataManage.delete();
	}
}
