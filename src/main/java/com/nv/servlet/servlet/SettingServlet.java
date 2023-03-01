package com.nv.servlet.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nv.commons.constant.SystemConstant;
import com.nv.commons.model.SystemSettingBO;
import com.nv.util.ExceptionUtils;
import com.nv.util.LogUtils;
import com.nv.util.RequestParser;
import com.nv.util.ResponseUtils;

/*
/setting/SettingServlet
*/
@WebServlet("/setting/SettingServlet/*")
public class SettingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public SettingServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) {

		try {
			String pathInfo = request.getPathInfo();
			
			if ("/updateSetting".equals(pathInfo)) {
				updateSetting(request, response);
			}
		} catch (Exception e) {
			String uuid = UUID.randomUUID().toString();
			LogUtils.setting.error(ExceptionUtils.createErrorMsg(e, uuid), e);
			ResponseUtils.respondErrorUUID(response, uuid);
		}
	}

	private void updateSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = RequestParser.getStringParameter(request, Integer.MAX_VALUE, "key", null);
		String value = RequestParser.getStringParameter(request, Integer.MAX_VALUE, "value", null);
		boolean success = SystemSettingBO.updateSettingByField(key, value);
		if(success) {
			ResponseUtils.sendResponse(response, SystemConstant.SUCCESS);
		}else {
			ResponseUtils.sendResponse(response, SystemConstant.FAIL);
		}
	}
}
