package clonegod.spring.framework.web.context;

import javax.servlet.ServletContext;

import clonegod.spring.framework.context.ApplicationContext;

public class WebApplicationUtil {

	private static ServletContext servletContext0;
	
	public static void setServletContext(ServletContext servletContext) {
		servletContext0 = servletContext;
	}
	
	public static ApplicationContext getParentFactory() {
		return (ApplicationContext) servletContext0.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

}
