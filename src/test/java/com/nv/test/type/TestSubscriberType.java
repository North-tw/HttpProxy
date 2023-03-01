package com.nv.test.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.model.database.RedisPubSubManager;
import com.nv.manager.SystemInfo;
import com.nv.test.util.SingleTestUtils;
import com.nv.util.LogUtils;

public enum TestSubscriberType {

	PubSubDemo {

		private String channel = String
			.join(SystemConstant.REDIS_TAG, "Channel", SystemConstant.REDIS_PREFIX, name()).toLowerCase();

		@Override
		protected boolean isAllowPublish() {
			return SystemInfo.isManagerServer();
		}

		@Override
		protected boolean isAllowedSubscribe() {
			return SystemInfo.isManagerServer() || SystemInfo.isProxyServer();
		}

		@Override
		protected long publishProcess(String data) {
			return RedisPubSubManager.get().getTopic(channel).publish(data);
		}

		@Override
		protected void subscribeProcess() {
			RedisPubSubManager.get().getTopic(channel).addListener(String.class, (channel, message) -> {
				LogUtils.system.info("do refresh cache");
				SingleTestUtils.getDataUtil().setOut(message);
				LogUtils.redisSubscribe.info("{} subscribing channel: {}, message: {}", name(), channel,
					message);
				synchronized (this) {
					this.notify();
				}
			});
		}

		@Override
		protected long countSubscribers() {
			return RedisPubSubManager.get().getTopic(channel).countSubscribers();
		}
	},;

	public final static List<TestSubscriberType> VALUES = Collections
		.unmodifiableList(Arrays.asList(TestSubscriberType.values()));

	/*
	 * 用來設定允許publish的Server
	 */
	protected abstract boolean isAllowPublish();

	/*
	 * 用來設定不允許subscribe的Server
	 */
	protected abstract boolean isAllowedSubscribe();

	public long publish(String data) {
		if (isAllowPublish()) {
			return publishProcess(data);
		}
		throw new RuntimeException(name() + " isn't supported!!! data:" + data);
	}

	protected abstract long publishProcess(String data);

	public boolean subscribe() {
		if (isAllowedSubscribe()) {
			subscribeProcess();
			return true;
		}
		return false;
	}

	protected abstract void subscribeProcess();

	protected abstract long countSubscribers();

	public long getSubscribersCount() {
		return countSubscribers();
	}
}
