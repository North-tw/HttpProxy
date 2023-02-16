package com.nv.test.base;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServlet;

import org.apache.commons.beanutils.ConstructorUtils;
import org.mockito.Mockito;

import com.nv.servlet.listener.AppInitListener;
import com.nv.test.util.ContextUtils;

public class TomcatMocker {

	private static final TomcatMocker instance = new TomcatMocker();

	// tomcat裡面servlet都僅有一個實體
	private Map<String, HttpServlet> httpServlets = new HashMap<>();

	private ServletContext servletContext;
	
	private ServletContextEvent sce = Mockito.mock(ServletContextEvent.class);
	private AppInitListener appInitListener = new AppInitListener();
	
	private TomcatMocker() {
		servletContext = ServletContextMocker.getMocker().setDefaultPath()
			.addInitParam("ServerID", ContextUtils.getInitParameter("ServerID"))
			.addInitParam("SiteName", ContextUtils.getInitParameter("SiteName"))
			.addInitParam("ServerType", ContextUtils.getInitParameter("ServerType"))
			.addInitParam("EventListenerFactoryClass",
				ContextUtils.getInitParameter("EventListenerFactoryClass"))
			.mock();
	}

	public static TomcatMocker getInstance() {
		return instance;
	}

	public void contextInitialized() {

		when(sce.getServletContext()).thenReturn(servletContext);

		appInitListener.contextInitialized(sce);
	}

	public HttpServlet getHttpServlet(final Class<? extends HttpServlet> servletClass) throws Exception {
		HttpServlet servlet = httpServlets.get(servletClass.getName());
		if (servlet == null) {
			servlet = ConstructorUtils.invokeConstructor(servletClass, null);
			httpServlets.put(servletClass.getName(), servlet);
		}
		return servlet;
	}

	public void contextDestroyed() {
		appInitListener.contextDestroyed(sce);
	}
}
