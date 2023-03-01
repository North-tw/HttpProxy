package com.nv.test.base;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.mockito.Mockito;

public class ServletContextMocker {

	private ServletContext servletContext = Mockito.mock(ServletContext.class);
	private Map<String, String> initParam = new HashMap<>();

	private ServletContextMocker() {
		when(servletContext.getInitParameter(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			return initParam.get(key);
		});

		when(servletContext.getInitParameterNames()).thenAnswer(aInvocation -> {
			return Collections.enumeration(initParam.keySet());
		});
	}

	public ServletContextMocker setDefaultPath() {

		// "" 表示根目錄
		when(servletContext.getContextPath()).thenReturn("");

		// 因為沒有實際的tomcat，所以指向到test專案根目錄
		String root = SystemPropertyMocker.getInstance().getRoot();

		// 設置項目真實的目錄
		when(servletContext.getRealPath(anyString())).thenReturn(root);
		// 設置/META-INF目錄，當前使用該目錄來保存配置
		when(servletContext.getRealPath("/META-INF")).thenReturn(root + File.separator + "META-INF");
		return this;
	}

	public ServletContextMocker addInitParam(String key, String value) {
		initParam.put(key, value);
		return this;
	}

	public ServletContext mock() {
		return servletContext;
	}

	public static ServletContextMocker getMocker() {
		return new ServletContextMocker();
	}

}
