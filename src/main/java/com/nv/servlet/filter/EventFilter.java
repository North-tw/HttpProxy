package com.nv.servlet.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nv.commons.constant.Setting;
import com.nv.commons.constant.SystemConstant;
import com.nv.manager.SystemInfo;
import com.nv.util.LogUtils;

/*
	/event/*
*/
@WebFilter(dispatcherTypes = {DispatcherType.REQUEST}, urlPatterns = {SystemConstant.URL_EVENT + "/*"})
public class EventFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = -6699548176066314572L;

	public EventFilter() {
		super();
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		// 重開ProxyServer前先停止接受事件，由另外一臺ProxyServer回覆DealerProxy，確保事件不遺漏。
		if (SystemInfo.isProxyServer()) {
			if (!Setting.STOP_RECEIVE_DEALER_EVENT) {
				LogUtils.system.info("[DealerCurrentStateServlet] stop dealer event listening...");
				return;
			} else {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
