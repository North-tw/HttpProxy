package com.nv.test;

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.model.database.RedisPubSubManager;
import com.nv.manager.SystemInfo;
import com.nv.test.base.ServletContextMocker;
import com.nv.test.type.TestSubscriberType;
import com.nv.test.util.ContextUtils;
import com.nv.test.util.SingleTestUtils;
import com.nv.util.LogUtils;

public class TestRedisPubSub {

	public static final String LOCK_KEY = "TestRedisPubSub.test";

	@BeforeClass
	public static void beforeClass() {
		ServletContext context = ServletContextMocker.getMocker()
			.addInitParam("ServerID", ContextUtils.getInitParameter("ServerID"))
			.addInitParam("SiteName", ContextUtils.getInitParameter("SiteName"))
			.addInitParam("ServerType", ContextUtils.getInitParameter("ServerType")).mock();

		SystemInfo.getInstance().init(context);
		RedisPubSubManager.init();
	}

	/**
	 * 不能保證TestSubscriberType.PubSubDemo.subscribeProcess註冊的Listener比wait晚執行，
	 * 因此需要設定等待時間，否則可能無限等待，為完成單元測試而有此設計
	 */
	@Test
	public void test1() throws InterruptedException {

		String msg = "test1 message";

		TestSubscriberType.PubSubDemo.subscribe();
		TestSubscriberType.PubSubDemo.publish(msg);

		synchronized (TestSubscriberType.PubSubDemo) {
			TestSubscriberType.PubSubDemo.wait(500);
		}

		assertEquals(msg, SingleTestUtils.getDataUtil().getOut());

	}

	@Test
	public void test2() throws InterruptedException {

		String msg = "test2 message";
		String channel = String
			.join(SystemConstant.REDIS_TAG, "Channel", SystemConstant.REDIS_PREFIX, "TestRedisPubSub.test2")
			.toLowerCase();

		for (int i = 0; i < 3; i++) {
			final String serverId = "TestRedisPubSub" + i;

			RedisPubSubManager.get().getTopic(channel).addListener(String.class,
				(messageChannel, message) -> {
					LogUtils.redisSubscribe.info("server: {}, subscribing channel: {}, message: {}", serverId,
						messageChannel, message);
					assertEquals(msg, message);
				});
		}

		RedisPubSubManager.get().getTopic(channel).publish(msg);

	}

	@After
	public void after() {
		SingleTestUtils.getDataUtil().clear();
	}

	@AfterClass
	public static void afterClass() {
		RedisPubSubManager.shutdown();
	}
}
