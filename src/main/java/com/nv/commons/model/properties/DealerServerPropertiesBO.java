package com.nv.commons.model.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.DealerServerPropertiesDAO;
import com.nv.commons.dto.properties.DealerServerProperties;
import com.nv.util.JSONUtils;

public class DealerServerPropertiesBO {

	public static Optional<DealerServerProperties> get(int tableId) {
		Optional<String> queryOptional = DealerServerPropertiesDAO.get(tableId);
		if (queryOptional.isPresent()) {
			DealerServerProperties data = JSONUtils.jsonToInstance(queryOptional.get(),
				DealerServerProperties.class);
			return Optional.ofNullable(data);
		} else {
			return Optional.empty();
		}
	}
}
