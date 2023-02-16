package com.nv.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerator;

public class ResponseUtils {

	public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

	public static void sendJsonResponse(HttpServletResponse response, Object obj) {
		sendJsonResponse(response, JSONUtils.toJsonStr(obj));
	}

	public static void sendJsonResponse(HttpServletResponse response, String json, boolean hasETag) {
		if (hasETag) {
			response.setHeader("ETag", String.valueOf(json.hashCode()));
		}
		sendJsonResponse(response, json);
	}

	public static void sendJsonResponse(HttpServletResponse response, String json) {
		response.setContentType(JSON_CONTENT_TYPE);
		ResponseUtils.sendResponse(response, json);
	}

	public static void sendJsonErrorResponse(HttpServletResponse response, Object errorObj) {
		sendJsonErrorResponse(response, JSONUtils.toJsonStr(errorObj));
	}

	public static void sendJsonErrorResponse(HttpServletResponse response, String errorMessage) {
		response.setContentType(JSON_CONTENT_TYPE);
		ResponseUtils.sendResponse(response, JSONUtils.getJSONStr("error", errorMessage));
	}

	public static void sendResponse(HttpServletResponse response, String message) {
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			response.setContentLength(StringUtils.countByteArrayLengthOfString(message));
			writer.write(message);
			writer.flush();
		} catch (Exception e) {
			LogUtils.responseError.error(e.getMessage(), e);
		}
	}

	/*
	 * return status: 200
	 */
	public static void respondSuccessWithMessage(HttpServletResponse response, String message) {
		ResponseUtils.sendJsonResponse(response,
			JSONUtils.getJSONStr("status", "200", "message", message));
	}

	/*
	 * return status: 200
	 */
	public static void respondSuccessWithTimestamp(HttpServletResponse response, String message) {
		ResponseUtils.sendJsonResponse(response, JSONUtils.getJSONStr("status", "200", "timestamp",
			String.valueOf(System.currentTimeMillis()), "message", message));
	}

	/*
	 * return status 500
	 */
	public static void respondError(HttpServletResponse response, String errorMessage) {
		ResponseUtils.sendJsonResponse(response,
			JSONUtils.getJSONStr("status", "500", "message", errorMessage));
	}

	public static void sendStatus(HttpServletResponse response, String statusCode) {
		sendStatus(response, statusCode, "");
	}

	public static void sendStatus(HttpServletResponse response, String statusCode, String description) {
		try (StringWriter out = new StringWriter();
			JsonGenerator jGenerator = JSONUtils.getFactory().createGenerator(out)) {
			jGenerator.writeStartObject();
			jGenerator.writeStringField("status", statusCode);
			if (StringUtils.isNotBlank(description)) {
				jGenerator.writeStringField("desc", description);
			}
			jGenerator.writeEndObject();
			jGenerator.flush();
			sendJsonResponse(response, out.toString());
		} catch (Exception e) {
			LogUtils.parseError.error(e.getMessage(), e);
		}
	}
}