package com.nv.commons.model.properties;

import java.util.Optional;

import com.nv.commons.dao.redis.properties.ResultServerPropertiesDAO;
import com.nv.commons.dto.properties.ResultServerProperties;
import com.nv.util.JSONUtils;

public class ResultServerPropertiesBO {

	public static Optional<ResultServerProperties> get() {
		return ResultServerPropertiesDAO.get();
	}
}
