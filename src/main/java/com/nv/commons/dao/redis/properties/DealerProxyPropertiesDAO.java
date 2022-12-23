package com.nv.commons.dao.redis.properties;

import java.util.Optional;

import org.redisson.api.RBucket;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.type.redis.KeyType;

public class DealerProxyPropertiesDAO {

	public final static String BASE_CACHE_NAME = String
		.join(SystemConstant.REDIS_TAG, SystemConstant.REDIS_PREFIX, KeyType.DealerProxyProperties.name())
		.toLowerCase();

	public static Optional<String> get() {
		RBucket<String> rBucket = RedisManager.get().getBucket(BASE_CACHE_NAME);
		return Optional.ofNullable(rBucket.get());
	}
}
