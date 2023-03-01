package com.nv.servlet.servlet;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nv.commons.constant.ApiCodeConstants;
import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.dealerserver.Message;
import com.nv.commons.listener.IEventListener;
import com.nv.manager.FactoryManager;
import com.nv.util.EncryptUtils;
import com.nv.util.JSONUtils;
import com.nv.util.LogUtils;
import com.nv.util.RequestParser;
import com.nv.util.ResponseUtils;

/*
	/event/ProxyServlet
*/
@WebServlet(SystemConstant.URL_EVENT + "/ProxyServlet/*")
public class ProxyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ProxyServlet() {
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

		String encryptMessage = RequestParser.getStringParameter(request, Integer.MAX_VALUE, "message", null);
		try {
			String messageJSON = EncryptUtils.desedeDecoder(encryptMessage,
				SystemConstant.DEALER_EVENT_AES_TOKEN);
			Message message = JSONUtils.jsonToInstance(messageJSON, Message.class);
			Optional<IEventListener> eventOp = FactoryManager.getInstance().getEventListenerFactory()
				.get(message.getEventType());
			if (eventOp.isPresent()) {
				eventOp.get().handleEvent(message.getBody());
			}
			ResponseUtils.sendStatus(response, ApiCodeConstants.CODE_SUCCESS);
		} catch (Exception e) {
			LogUtils.err.error(e.getMessage());
			ResponseUtils.sendStatus(response, ApiCodeConstants.CODE_FAIL);
		}
	}
}
