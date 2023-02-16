package com.nv.commons.dao.redis.properties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.redisson.api.RMap;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.properties.DealerServerProperties;
import com.nv.commons.model.database.RedisManager;
import com.nv.commons.type.redis.KeyType;
import com.nv.util.JSONUtils;

public class DealerServerPropertiesDAO {

	public final static String BASE_CACHE_NAME = String
		.join(SystemConstant.REDIS_TAG, SystemConstant.REDIS_PREFIX, KeyType.DealerServerProperties.name())
		.toLowerCase();

	public static Optional<DealerServerProperties> get(int tableId) {
		RMap<String, String> rMap = RedisManager.get().getMap(BASE_CACHE_NAME);
		DealerServerProperties data = JSONUtils.jsonToInstance(rMap.get(Integer.valueOf(tableId).toString()),
			DealerServerProperties.class);
		return Optional.ofNullable(data);
	}

	public static List<DealerServerProperties> getAll() {
		RMap<String, String> rMap = RedisManager.get().getMap(BASE_CACHE_NAME);
		return rMap.readAllMap().values().stream()
			.map(str -> JSONUtils.jsonToInstance(str, DealerServerProperties.class))
			.collect(Collectors.toList());
	}
}
