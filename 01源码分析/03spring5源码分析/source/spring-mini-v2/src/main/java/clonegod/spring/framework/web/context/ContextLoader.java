package clonegod.spring.framework.web.context;

import javax.servlet.ServletContext;

import clonegod.spring.framework.web.context.support.MySimpleWebApplicationContext;

/**
 * 初始化 Root WebApplicationContext 
 */
public class ContextLoader {

	public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
	
	public WebApplicationContext rootContext;

	protected WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		WebApplicationUtil.setServletContext(servletContext);
		
		long startTime = System.currentTimeMillis();
		if (this.rootContext == null) {
			// 创建 WebApplicationContext 实例
			this.rootContext = createWebApplicationContext(servletContext);
		}
		// 如果是可配置的ApplicationContext，调用refresh()
		if (this.rootContext instanceof ConfigurableWebApplicationContext) {
			ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.rootContext;
			configureAndRefreshWebApplicationContext(cwac, servletContext);
		}
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.rootContext);
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("------>>> Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
		
		return rootContext;
	}
	
	private WebApplicationContext createWebApplicationContext(ServletContext servletContext) {
		return new MySimpleWebApplicationContext();
	}

	private void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
		try {
			wac.setServletContext(sc);
			String configLocationParam = sc.getInitParameter(CONFIG_LOCATION_PARAM);
			if (configLocationParam != null) {
				wac.setConfigLocations(configLocationParam);
			}
			wac.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
}
