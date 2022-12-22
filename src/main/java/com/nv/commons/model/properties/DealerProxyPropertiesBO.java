package com.nv.commons.model.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.DealerProxyPropertiesDAO;
import com.nv.commons.dto.properties.DealerProxyProperties;
import com.nv.util.JSONUtils;

public class DealerProxyPropertiesBO {

	public static Optional<DealerProxyProperties> get() {
		Optional<String> queryOptional = DealerProxyPropertiesDAO.get();
		if (queryOptional.isPresent()) {
			DealerProxyProperties data = JSONUtils.jsonToInstance(queryOptional.get(),
				DealerProxyProperties.class);
			return Optional.ofNullable(data);
		} else {
			return Optional.empty();
		}
	}
}
