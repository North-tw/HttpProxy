package com.nv.commons.model.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.DealerProxyPropertiesDAO;
import com.nv.commons.dto.properties.DealerProxyProperties;

public class DealerProxyPropertiesBO {

	public static Optional<DealerProxyProperties> get() {
		return DealerProxyPropertiesDAO.get();
	}
}
