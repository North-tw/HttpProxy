package com.nv.manager.cache.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.ResultServerPropertiesDAO;
import com.nv.commons.dto.properties.ResultServerProperties;
import com.nv.commons.dto.properties.ResultServerPropertiesView;

public class ResultServerCache {
	private static ResultServerCache instance = new ResultServerCache();
	private ResultServerPropertiesView cache;

	public static ResultServerCache getInstance() {
		return instance;
	}

	private ResultServerCache() {
	}

	public void init() {
		update();
	}

	public void update() {
		Optional<ResultServerProperties> queryResult = ResultServerPropertiesDAO.get();

		if (queryResult.isEmpty()) {
			throw new IllegalArgumentException("ResultServerPropertiesDAO.get: is empty");
		}

		cache = new ResultServerPropertiesView(queryResult.get());
	}

	public ResultServerPropertiesView get() {
		return cache;
	}
}
