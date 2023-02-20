package com.nv.test.base;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class RequestMocker {

	public enum HttpMethodType {
		GET, POST
	}

	protected HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

	private Class<? extends HttpServlet> servletClass;

	protected Map<String, String> parameters = new HashMap<>();

	private Map<String, Object> attributes = new HashMap<>();

	private List<Class<? extends Filter>> filterList = new ArrayList<>();

	// 儲存header參數
	private Map<String, String> headers = new HashMap<>();

	// 儲存cookie參數
	private Set<Cookie> cookies = new HashSet<>();

	// 處理forward and redirect
	private RequestDispatcher requestDispatcher;

	// for response，一個時間只會有一個為非null
	private StringWriter stringWriter = null;
	private ByteArrayOutputStream outputStream = null;

	public RequestMocker(Class<? extends HttpServlet> servletClass, String pathInfo) {
		this(servletClass, pathInfo, (HttpSession) null);
	}

	public RequestMocker(Class<? extends HttpServlet> servletClass, String pathInfo, String sessionID) {
		this(servletClass, pathInfo, SessionMocker.getInstance(sessionID).getHttpSession());
	}

	public RequestMocker(Class<? extends HttpServlet> servletClass, String pathInfo, HttpSession session) {

		this.servletClass = servletClass;

		when(request.getScheme()).thenReturn("http");

		when(request.getMethod()).thenReturn(HttpMethodType.POST.name());

		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(request.getServerName()).thenReturn("127.0.0.1");

		headers.put("User-Agent",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		when(request.getHeader(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			return headers.get(key);
		});

		// 設定path info 跟 URI
		when(request.getPathInfo()).thenReturn(pathInfo);
		when(request.getRequestURI()).thenAnswer(aInvocation -> request.getPathInfo());

		if (session != null) {
			when(request.getSession(anyBoolean())).thenReturn(session);
			when(request.getSession()).thenReturn(session);
		}

		when(request.getParameter(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			return parameters.get(key);
		});

		when(request.getParameterValues(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			String value = parameters.get(key);
			if (value == null)
				return null;

			return new String[]{value};
		});

		when(request.getParameterNames()).thenReturn(Collections.enumeration(parameters.keySet()));

		Mockito.doAnswer((Answer<Object>) aInvocation -> {
			Cookie cookie = (Cookie) aInvocation.getArguments()[0];
			this.cookies.add(cookie);
			return null;
		}).when(response).addCookie(anyObject());

		when(request.getCookies()).thenAnswer(aInvocation -> this.cookies.toArray(new Cookie[0]));

		when(request.getAttribute(anyString())).thenAnswer(aInvocation -> {
			String key = (String) aInvocation.getArguments()[0];
			return attributes.get(key);
		});

		Mockito.doAnswer(invocation -> {
			String key = (String) invocation.getArguments()[0];
			Object value = invocation.getArguments()[1];
			return attributes.put(key, value);
		}).when(request).setAttribute(anyString(), anyObject());

		try {
			when(request.getInputStream()).thenReturn(new ServletInputStreamMocker(""));

			Mockito.doAnswer(invocation -> {
				this.stringWriter = new StringWriter();
				return new PrintWriter(stringWriter) {

					public String toString() {
						return stringWriter.toString();
					}
				};
			}).when(response).getWriter();

			Mockito.doAnswer(invocation -> {
				this.outputStream = new ByteArrayOutputStream();
				return new ServletOutputStreamMocker(this.outputStream);
			}).when(response).getOutputStream();

			Mockito.doAnswer(invocation -> {
				String path = (String) invocation.getArguments()[0];
				this.requestDispatcher = Mockito.mock(RequestDispatcher.class);
				when(this.requestDispatcher.toString()).thenReturn("Redirect to " + path);
				return null;
			}).when(response).sendRedirect(anyString());
		} catch (Exception e) {

		}

		Mockito.doAnswer((Answer<Object>) aInvocation -> {
			Cookie cookie = (Cookie) aInvocation.getArguments()[0];
			this.cookies.add(cookie);
			return null;
		}).when(response).addCookie(anyObject());

		when(request.getCookies()).thenAnswer(aInvocation -> this.cookies.toArray(new Cookie[0]));

		when(request.getRequestDispatcher(anyString()))
			.thenAnswer((Answer<RequestDispatcher>) aInvocation -> {
				String path = (String) aInvocation.getArguments()[0];
				this.requestDispatcher = Mockito.mock(RequestDispatcher.class);
				when(this.requestDispatcher.toString()).thenReturn("Forword to " + path);
				return this.requestDispatcher;
			});

	}

	public void setServerName(String serverName) {
		when(request.getServerName()).thenReturn(serverName);
	}

	public void setScheme(String scheme) {
		when(request.getScheme()).thenReturn(scheme);
	}

	public void setJsonData(String postJsonData) throws IOException {
		when(request.getInputStream()).thenAnswer((Answer<ServletInputStream>) invocation -> {

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(postJsonData.getBytes());

			return new ServletInputStream() {

				public int read() {
					return byteArrayInputStream.read();
				}

				@Override
				public boolean isFinished() {
					return true;
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener listener) {
				}
			};
		});

		when(request.getReader()).thenAnswer(
			(Answer<BufferedReader>) invocation -> new BufferedReader(new StringReader(postJsonData)));
	}

	public void setInputStream(String content) throws IOException {
		when(request.getInputStream()).thenReturn(new ServletInputStreamMocker(content));
	}

	public void setMethod(HttpMethodType type) {
		when(request.getMethod()).thenReturn(type.name());
	}

	public void setRemoteAddr(String ip) {
		when(request.getRemoteAddr()).thenReturn(ip);
	}

	public void setParameter(String key, String value) {
		this.parameters.put(key, value);
	}

	public void setParameters(Map<String, String> parameters) {
		if (parameters == null) {
			return;
		}
		this.parameters.putAll(parameters);
	}

	public void addCookie(String key, String value) {
		if (key == null || value == null) {
			return;
		}
		this.cookies.add(new Cookie(key, value));
	}

	public void addCookie(Cookie cookie) {
		if (cookie == null) {
			return;
		}
		this.cookies.add(cookie);
	}

	public void setCookies(Set<Cookie> cookies) {
		if (cookies == null) {
			return;
		}
		this.cookies.addAll(cookies);
	}

	public void setHeaders(Map<String, String> headers) {
		if (headers == null) {
			return;
		}
		this.headers.putAll(headers);
	}

	public void setHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public void setContentType(String value) {
		when(request.getContentType()).thenReturn(value);
	}

	public RequestMocker withFilter(Class<? extends Filter>[] filters) throws Exception {
		for (Class<? extends Filter> filterClass : filters) {
			this.filterList.add(filterClass);
		}
		return this;
	}

	/**
	 * @return 回傳null表示filter測試正常，沒有發生轉址
	 * @throws Exception
	 */
	// 為了讓情境單純，這邊也可以採另外測試filter的部分，不跟servlet混淆
	public RequestDispatcher testFilter() throws Exception {
		FilterChain filterChain = Mockito.mock(FilterChain.class);

		Iterator<Class<? extends Filter>> iterator = filterList.iterator();
		if (iterator.hasNext()) {
			Mockito.doAnswer((Answer<Object>) aInvocation -> {
				if (iterator.hasNext()) {
					ConstructorUtils.invokeConstructor(iterator.next(), null).doFilter(request, response,
						filterChain);
				}
				return null;
			}).when(filterChain).doFilter(anyObject(), anyObject());

			ConstructorUtils.invokeConstructor(iterator.next(), null).doFilter(request, response, filterChain);
		}
		return this.requestDispatcher;
	}

	public String execute() {
		try {
			if (this.filterList.size() > 0) {
				RequestDispatcher requestDispatcher = testFilter();
				// 表示發生轉址，沒有正常執行完filter
				if (requestDispatcher != null) {
					return requestDispatcher.toString();
				}
			}

			if (servletClass == null) {
				return null;
			}
			HttpServlet httpServlet = TomcatMocker.getInstance().getHttpServlet(servletClass);

			httpServlet.service(request, response);

			// 依照tomcat規格，一個時間點只會有一個被執行
			if (this.requestDispatcher != null) {
				// servlet內發生轉址
				return this.requestDispatcher.toString();
			} else if (stringWriter != null) {
				return stringWriter.toString();
			} else if (outputStream != null) {
				return new String(outputStream.toByteArray());
			}
		} catch (Exception e) {
			Assert.fail();
		}
		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public static class ServletOutputStreamMocker extends ServletOutputStream {

		// 定義檔案輸出流
		private ByteArrayOutputStream bos;

		public ServletOutputStreamMocker(ByteArrayOutputStream bos) {
			this.bos = bos;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new RuntimeException("do not support this method");
		}

		@Override
		public void write(int b) {
			bos.write(b);
		}

		@Override
		public void close() {
			try {
				bos.close();
			} catch (IOException e) {
			}
		}
	}

	public static class ServletInputStreamMocker extends ServletInputStream {

		private InputStream inputStream;

		ServletInputStreamMocker(String string) {
			this.inputStream = IOUtils.toInputStream(string, "UTF-8");
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
		}
	}
}
