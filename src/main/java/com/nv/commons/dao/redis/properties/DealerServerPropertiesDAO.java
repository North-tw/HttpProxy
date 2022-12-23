package com.nv.commons.dao.redis.properties;

import java.util.Optional;

import org.redisson.api.RMap;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.type.redis.KeyType;

public class DealerServerPropertiesDAO {

	public final static String BASE_CACHE_NAME = String
		.join(SystemConstant.REDIS_TAG, SystemConstant.REDIS_PREFIX, KeyType.DealerServerProperties.name())
		.toLowerCase();

	public static Optional<String> get(int tableId) {
		RMap<String, String> rMap = RedisManager.get().getMap(BASE_CACHE_NAME);
		return Optional.ofNullable(rMap.get(Integer.valueOf(tableId).toString()));
	}
}
