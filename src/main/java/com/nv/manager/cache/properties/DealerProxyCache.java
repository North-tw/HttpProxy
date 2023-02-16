package com.nv.manager.cache.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.DealerProxyPropertiesDAO;
import com.nv.commons.dto.properties.DealerProxyProperties;
import com.nv.commons.dto.properties.DealerProxyPropertiesView;

public class DealerProxyCache {

	private static DealerProxyCache instance = new DealerProxyCache();
	private DealerProxyPropertiesView cache;

	public static DealerProxyCache getInstance() {
		return instance;
	}

	private DealerProxyCache() {
	}

	public void init() {
		update();
	}

	public void update() {
		Optional<DealerProxyProperties> queryResult = DealerProxyPropertiesDAO.get();

		if (queryResult.isEmpty()) {
			throw new IllegalArgumentException("DealerProxyPropertiesDAO.get: is empty");
		}

		cache = new DealerProxyPropertiesView(queryResult.get());
	}

	public DealerProxyPropertiesView get() {
		return cache;
	}
}
