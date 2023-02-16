package com.nv.commons.dao.redis.properties;

import java.util.Optional;

import org.redisson.api.RBucket;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.properties.DealerProxyProperties;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.type.redis.KeyType;
import com.nv.util.JSONUtils;

public class DealerProxyPropertiesDAO {

	public final static String BASE_CACHE_NAME = String
		.join(SystemConstant.REDIS_TAG, SystemConstant.REDIS_PREFIX, KeyType.DealerProxyProperties.name())
		.toLowerCase();

	public static Optional<DealerProxyProperties> get() {
		RBucket<String> rBucket = RedisManager.get().getBucket(BASE_CACHE_NAME);
		DealerProxyProperties data = JSONUtils.jsonToInstance(rBucket.get(), DealerProxyProperties.class);
		return Optional.ofNullable(data);
	}
}
