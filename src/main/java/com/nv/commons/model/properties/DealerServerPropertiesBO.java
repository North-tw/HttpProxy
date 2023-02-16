package com.nv.commons.model.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.DealerServerPropertiesDAO;
import com.nv.commons.dto.properties.DealerServerProperties;
import com.nv.util.JSONUtils;

public class DealerServerPropertiesBO {

	public static Optional<DealerServerProperties> get(int tableId) {
		return DealerServerPropertiesDAO.get(tableId);
	}
}
