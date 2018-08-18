package clonegod.spring.framework.web.context;

import javax.servlet.ServletContext;

import clonegod.spring.framework.context.ApplicationContext;

/**
 * 提供获取ServletContext的接口
 *
 */
public interface WebApplicationContext extends ApplicationContext {

	String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
	
	/**
	 * Return the standard Servlet API ServletContext for this application.
	 */
	ServletContext getServletContext();
	
}
