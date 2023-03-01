package com.nv.manager.cache.properties;

import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.nv.commons.dao.redis.properties.DealerServerPropertiesDAO;
import com.nv.commons.dto.properties.DealerServerProperties;
import com.nv.commons.dto.properties.DealerServerPropertiesView;

public class DealerServerCache {

	private static DealerServerCache instance = new DealerServerCache();
	private NavigableMap<Integer, DealerServerPropertiesView> cache = new ConcurrentSkipListMap<>();

	public static DealerServerCache getInstance() {
		return instance;
	}

	private DealerServerCache() {
	}

	public void init() {
		update();
	}

	public void update() {
		List<DealerServerProperties> queryResult = DealerServerPropertiesDAO.getAll();

		if (queryResult.isEmpty()) {
			throw new IllegalArgumentException("DealerServerPropertiesDAO.getAll: is empty");
		}
		
		NavigableMap<Integer, DealerServerPropertiesView> newData = new ConcurrentSkipListMap<>();
		queryResult.forEach(p -> newData.put(p.getTableId(), new DealerServerPropertiesView(p)));
		
		cache = newData;
	}

	public DealerServerPropertiesView get(int tableId) {
		return cache.get(tableId);
	}
}
