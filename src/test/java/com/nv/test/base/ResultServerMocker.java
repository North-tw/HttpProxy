package com.nv.test.base;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.nv.commons.constant.SystemConstant;
import com.nv.test.util.SingleTestUtils;
import com.nv.test.util.WireMockServerUtils;
import com.nv.util.EncryptUtils;
import com.nv.util.ExceptionUtils;
import com.nv.util.LogUtils;

public class ResultServerMocker {

	private final static ResultServerMocker instance = new ResultServerMocker();

	private WireMockServer server;

	public static ResultServerMocker getInstance() {
		return instance;
	}

	private ResultServerMocker() {
		super();
	}

	@SuppressWarnings("unchecked")
	public void start() {
		// 限定只能模擬本機
		server = new WireMockServer(
			WireMockConfiguration.wireMockConfig().extensions(ResultServerTransformer.class).port(8090));
		server.start();
	}

	public void shutdown() {
		if(server != null) {
			server.shutdown();
		}
	}

	public static class ResultServerTransformer extends ResponseDefinitionTransformer {

		@Override
		public String getName() {
			return this.getClass().getSimpleName();
		}

		@Override
		public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition,
			FileSource files, Parameters parameters) {
			ResponseDefinitionBuilder response = WireMock.aResponse();
			response.withHeader("Content-Type", "application/json");
			Map<String, String> params = WireMockServerUtils.getParameter(request);
			String x = params.get("x");
			String x1 = params.get("x1");

			try {
				LogUtils.system.info("message before desedeDecoder = {}", x);
				String message = EncryptUtils.desedeDecoder(x, SystemConstant.DEALER_EVENT_AES_TOKEN);
				LogUtils.system.info("message after desedeDecoder = {}", message);

				SingleTestUtils.getUtil().setOut(message);

				LogUtils.system.info("event before desedeDecoder = {}", x1);
				String event = EncryptUtils.desedeDecoder(x1, SystemConstant.DEALER_EVENT_AES_TOKEN);
				LogUtils.system.info("event after desedeDecoder = {}", event);
			} catch (Exception ex) {
				LogUtils.err.error(ex.getMessage(), ex);
				throw ExceptionUtils.amendToUncheckedException(ex);
			}
			response.withBody(SystemConstant.SUCCESS);
			return response.build();
		}

	}
}
