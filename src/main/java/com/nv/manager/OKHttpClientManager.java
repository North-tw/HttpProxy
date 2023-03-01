package com.nv.manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;

import com.nv.expandUtil.util.ExceptionUtils;
import com.nv.expandUtil.util.StringUtils;
import com.nv.util.LogUtils;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * 使用okhttp，並做封裝，避免耦合 Alan 2021.08.20
 */
public class OKHttpClientManager {

	@FunctionalInterface
	public interface RequestProcessor {

		void process(Request.Builder request);
	}

	@FunctionalInterface
	public interface ConfigProcessor {

		void process(OkHttpClient.Builder client);
	}

	public interface HttpCallBack {

		void onResponse(HTTPResponse response);

		void onFailure(BaseRequest request, IOException e);
	}

	Logger log = LogUtils.system;

	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

	private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

	private static final String DEFAULT_USER_AGENT = "mozilla/4.0 (compatible; msie 8.0; windows nt 6.1; wow64; trident/4.0; slcc2; .net clr 2.0.50727; .net clr 3.5.30729; .net clr 3.0.30729; media center pc 6.0; masn)";

	// 請求建立連接的超時時間，單位毫秒
	public static int CONNECTION_TIMEOUT = 5 * 1000;

	/**
	 * 建立連接後，獲取數據的閒置超時時間，單位毫秒。
	 * 兩個數據封包之間的時間大於該時間則認為超時，假設設置1秒超時，如果每隔0.8秒傳輸一次數據，傳輸10次，總共8秒，這樣是不超時的
	 */
	public static int SOCKET_HOLD_TIMEOUT = 30 * 1000;

	// 對同一站點最大可以發起的連線數，Mexico有四臺WebServer，連外爲NAT，最大瞬時可以達2800
	public static int MAX_PER_ROUTE = 700;

	// 可以同時發起的連線數量
	public static int MAX_TOTAL = 1400;

	// 執行緒池
	// 空閒60秒的多餘thread會銷毀
	private static ThreadPoolExecutor service = new ThreadPoolExecutor(//
		10, // 初始值
		MAX_TOTAL, // 最大值
		3L, TimeUnit.MINUTES, // 空閒3分鐘的多餘thread會銷毀
		new SynchronousQueue<>(), //
		(runnable, executor) -> {
			if (!executor.isShutdown()) {
				LogUtils.system.error("All threads are busy currently in the http thread pool");
				runnable.run();
			}
		});

	// 預設忽略證書的驗證
	private TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}
	}};

	/**
	 * 配置一個HostnameVerifier來忽略host驗證 否則可能會出現請求證書和伺服器的證書不一致的錯誤
	 *
	 * @return
	 */
	private HostnameVerifier hostnameVerifier = new HostnameVerifier() {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private OkHttpClient client;

	private static OKHttpClientManager instance = new OKHttpClientManager();

	private OKHttpClientManager() {
		try {

			OkHttpClient.Builder client = new OkHttpClient.Builder();

			// 對同一個網站最多保持多少的 keep-alive connection
			client.connectionPool(new ConnectionPool(10, 6, TimeUnit.SECONDS));
			client.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
			client.readTimeout(SOCKET_HOLD_TIMEOUT, TimeUnit.MILLISECONDS);
			client.writeTimeout(SOCKET_HOLD_TIMEOUT, TimeUnit.MILLISECONDS);

			// retry機制由程式精準控制
			client.retryOnConnectionFailure(false);

			// 設定thread pool
			Dispatcher dispatcher = new Dispatcher(service);

			// 可以同時發起的連線數量
			dispatcher.setMaxRequests(MAX_TOTAL);

			// 對同一站點最大可以發起的連線數
			dispatcher.setMaxRequestsPerHost(MAX_PER_ROUTE);

			client.dispatcher(dispatcher);

			// 接收所有SSL憑證，並省略驗證
			client.sslSocketFactory(sslSocketFactory(), (X509TrustManager) trustAllCerts[0]);
			client.hostnameVerifier(hostnameVerifier);

			this.client = client.build();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	public String monitorTotalStats() {
		StringJoiner routeList = new StringJoiner(", ");
		this.client.interceptors().forEach(r -> {
			routeList.add(r.toString());
		});

		return "Used = " + this.client.connectionPool().connectionCount() + ", Idle = "
			+ this.client.connectionPool().idleConnectionCount() + ", Max = "
			+ this.client.dispatcher().getMaxRequests() + ", MaxRequestsPerHost = "
			+ this.client.dispatcher().getMaxRequestsPerHost() + ", Routes = "
			+ this.client.interceptors().size() + ", Routes List = " + routeList.toString();
	}

	public static OKHttpClientManager getInstance() {
		return instance;
	}

	public SSLSocketFactory sslSocketFactory() {
		try {
			// 信任任何連結
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public HttpGetRequest getHttpGetRequest(String url) {
		return new HttpGetRequest(url);
	}

	public HttpPostRequest getHttpPostRequest(String url) {
		return new HttpPostRequest(url);
	}

	public HttpPostRequest getHttpPostRequest(String url, boolean closeConnection) {
		HttpPostRequest req = new HttpPostRequest(url);
		if (closeConnection) {
			req.addHeader("Connection", "close");
		}
		return req;
	}
	
	public HttpJsonPostRequest getHttpJsonPostRequest(String url) {
		return new HttpJsonPostRequest(url);
	}

	public HttpHeadRequest getHttpHeadRequest(String url) {
		return new HttpHeadRequest(url);
	}

	public HTTPResponse execute(BaseRequest request) {
		return request.execute();
	}

	public abstract class BaseRequest {

		protected Request.Builder request = new Request.Builder();

		protected String url;

		protected List<RequestProcessor> requestProcessors = null;

		protected List<ConfigProcessor> configProcessors = null;

		protected Map<String, String> parameters = null;

		protected String charset = DEFAULT_CHARSET;

		protected void addProcessor(RequestProcessor processor) {
			if (this.requestProcessors == null) {
				this.requestProcessors = new ArrayList<>();
			}
			this.requestProcessors.add(processor);
		}

		protected void addConfig(ConfigProcessor processor) {
			if (this.configProcessors == null) {
				this.configProcessors = new ArrayList<>();
			}
			this.configProcessors.add(processor);
		}

		public BaseRequest() {
			// 放入相關預設值
			request.header("User-Agent", DEFAULT_USER_AGENT);
		}

		/**
		 * 指定 Timeout 時間 單位為毫秒
		 *
		 * @param connectTimeout
		 *            設置連接超時時間，單位毫秒
		 * @param socketTimeout
		 *            請求獲取數據的超時時間，單位毫秒。 如果訪問一個接口，多少時間內無法返回數據，就直接放棄此次調用。
		 * @return
		 */
		public void setTimeout(int socketTimeout, int connectTimeout) {
			addConfig((client) -> {
				client.readTimeout(socketTimeout, TimeUnit.MILLISECONDS);
				client.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
			});
		}

		public void setTimeout(int socketTimeout) {
			addConfig((client) -> {
				client.readTimeout(socketTimeout, TimeUnit.MILLISECONDS);
			});
		}

		public void setConnectTimeout(int connectTimeout) {
			addConfig((client) -> {
				client.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
			});
		}

		// 設定代理伺服器
		public void setProxy(String proxyAddress, int proxyPort) {
			addConfig((client) -> {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
				client.proxy(proxy);
			});
		}

		// 設定代理伺服器
		public void setProxy(String proxyAddress, int proxyPort, String proxyUserName, String proxyPassword) {
			Authenticator proxyAuthenticator = new Authenticator() {

				@Override
				public Request authenticate(Route route, Response response) throws IOException {
					String credential = Credentials.basic(proxyUserName, proxyPassword);
					return response.request().newBuilder().header("Proxy-Authorization", credential).build();
				}
			};
			addConfig((client) -> {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
				client.proxy(proxy);
				client.proxyAuthenticator(proxyAuthenticator);
			});
		}

		public void addParameter(String key, String value) {
			getParameters().put(key, value);
		}

		public void setParameters(Map<String, String> parameters) {
			getParameters().putAll(parameters);
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		public void addInterceptor(Interceptor interceptor) {
			addConfig((client) -> {
				client.addInterceptor(interceptor);
			});
		}

		public Map<String, String> getParameters() {
			if (this.parameters == null) {
				this.parameters = new HashMap<>();
			}
			return this.parameters;
		}

		public void addHeader(String key, String value) {
			addProcessor((request) -> {
				request.header(key, value);
			});
		}

		public void setHeaders(Map<String, String> headers) {
			addProcessor((request) -> {
				parameters.entrySet().stream().forEach(e -> {
					request.header(e.getKey(), e.getValue());
				});
			});
		}

		public void processRequest() {
			Optional.ofNullable(requestProcessors)
				.ifPresent(ps -> ps.stream().forEach(p -> p.process(request)));
		}

		public String getUrl() {
			return url;
		}

		public OkHttpClient processorConfig() {
			// 判斷是否有特別配置
			OkHttpClient client;
			if (this.configProcessors != null) {
				OkHttpClient.Builder newClient = OKHttpClientManager.this.client.newBuilder();
				for (ConfigProcessor processor : configProcessors) {
					processor.process(newClient);
				}
				client = newClient.build();
			} else {
				client = OKHttpClientManager.this.client;
			}

			return client;
		}

		// 同步呼叫
		public final HTTPResponse execute() {
			request.url(url);
			Response response = null;
			try {
				// 處理request的相關設定
				processRequest();

				// 處理配置的相關設定
				OkHttpClient client = processorConfig();

				Call call = client.newCall(this.request.build());

				response = call.execute();
				if (response.code() != 200 && response.code() != 404) {
					LogUtils.httpClient.info("HTTP Status-Code (" + response.code()
						+ ") Length w/o headers: [" + url.length() + "] " + url);
				}
				return HTTPResponse.create(response);
			} catch (Exception e) {
				LogUtils.httpClient.error("url:" + url + " ; " + e.getMessage(), e);
				throw ExceptionUtils.amendToUncheckedException(e);
			} finally {
				if (response != null) {
					response.close();
				}
			}
		}

		// 非同步呼叫
		public final void enqueue(HttpCallBack httpCallBack) {
			request.url(url);

			// 處理request的相關設定
			processRequest();

			// 處理配置的相關設定
			OkHttpClient client = processorConfig();

			Call call = client.newCall(this.request.build());

			BaseRequest self = this;

			// HttpCallBack
			call.enqueue(new Callback() {

				@Override
				public void onResponse(Call call, Response response) {
					// 連線成功，自response取得連線結果
					try {
						httpCallBack.onResponse(HTTPResponse.create(response));
					} catch (Exception e) {
						LogUtils.httpClient.error("url:" + url + " ; " + e.getMessage(), e);
						throw ExceptionUtils.amendToUncheckedException(e);
					} finally {
						if (response != null) {
							response.close();
						}
					}
				}

				@Override
				public void onFailure(Call call, IOException e) {
					// 這裡的失敗指的是沒有網絡請求發送不出去，或者請求地址有誤找不到服務器這類情況
					// 如果服務器返回的是404錯誤也說明請求到服務器了，屬於請求成功的情況，要在下面的方法中處理
					httpCallBack.onFailure(self, e);
				}
			});
		}
	}
	/**
	 * 1. 對外不暴露HttpClient，避免耦合 2. 因為參數組合過多，所以採用builder的設計模式
	 */
	public class HttpGetRequest extends BaseRequest {

		public HttpGetRequest(String url) {
			super();
			super.url = url;
		}

		@Override
		public void processRequest() {
			// 處理 get request特別的部分
			if (super.parameters != null && super.parameters.size() > 0) {
				StringBuilder sb = new StringBuilder(url).append("?");

				parameters.entrySet().stream().forEach(e -> {
					sb.append(e.getKey()).append("=").append(StringUtils.URLEncode(e.getValue(), charset))
						.append("&");
				});
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
				super.request.url(sb.toString());
			} else {
				super.request.url(url);
			}
			super.processRequest();
		}
	}

	/**
	 * 1. 對外不暴露HttpClient，避免耦合 2. 因為參數組合過多，所以採用builder的設計模式
	 */
	public class HttpPostRequest extends BaseRequest {

		public HttpPostRequest(String url) {
			super();
			super.url = url;
		}

		private RequestBody getRequestBody(Map<String, String> params) {
			FormBody.Builder builder = new FormBody.Builder();
			for (Map.Entry<String, String> m : params.entrySet()) {
				builder.add(m.getKey(), m.getValue());
			}
			return builder.build();
		}

		@Override
		public void processRequest() {
			// 處理 post request特別的部分
			if (super.parameters != null) {
				RequestBody body = getRequestBody(super.parameters);
				super.request.post(body);
			}

			super.processRequest();
		}

	}

	public class HttpHeadRequest extends BaseRequest {

		public void addParameter(String key, String value) {
			throw new UnsupportedOperationException();
		}

		public void setParameters(Map<String, String> parameters) {
			throw new UnsupportedOperationException();
		}

		public HttpHeadRequest(String url) {
			super();
			super.url = url;
		}

		@Override
		public void processRequest() {
			super.request.head();
			super.processRequest();
		}

	}

	public class HttpJsonPostRequest extends BaseRequest {

		public void addParameter(String key, String value) {
			throw new UnsupportedOperationException();
		}

		public void setParameters(Map<String, String> parameters) {
			throw new UnsupportedOperationException();
		}

		public HttpJsonPostRequest(String url) {
			super();
			super.url = url;
		}

		public void setJson(String content) {
			addProcessor((request) -> {
				RequestBody body = RequestBody.create(content, MEDIA_TYPE_JSON);
				request.post(body);
			});
		}

	}

	public final static class HTTPResponse {

		private int statusCode;

		private String content;

		private Headers headers;

		// 統一method規則採用create
		public final static HTTPResponse create(Response response) {
			try {
				return new HTTPResponse(response.code(), response.body().string(), response.headers());
			} catch (IOException e) {
				throw ExceptionUtils.amendToUncheckedException(e);
			}
		}

		// 設定為private，避免外部不正確使用
		private HTTPResponse(int statusCode, String content, Headers headers) {
			super();

			this.statusCode = statusCode;
			this.content = content;
			this.headers = headers;
		}

		// 取得狀態碼
		public int getStatusCode() {
			return statusCode;
		}

		// 取得回應內容
		public String getContent() {
			return content;
		}

		public String getHeader(String name) {
			return headers.get(name);
		}
	}
}
