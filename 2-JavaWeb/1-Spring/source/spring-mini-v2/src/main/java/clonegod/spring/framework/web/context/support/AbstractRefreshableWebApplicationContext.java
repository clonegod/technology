package clonegod.spring.framework.web.context.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


import clonegod.spring.framework.context.support.AbstractRefreshableConfigApplicationContext;
import clonegod.spring.framework.web.context.ConfigurableWebApplicationContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext 
	implements ConfigurableWebApplicationContext{
	
	/** Servlet context that this context runs in */
	private ServletContext servletContext;

	/** Servlet config that this context runs in, if any */
	private ServletConfig servletConfig;

	public AbstractRefreshableWebApplicationContext() {
		setDisplayName("Root WebApplicationContext");
	}


	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
		if (servletConfig != null && this.servletContext == null) {
			setServletContext(servletConfig.getServletContext());
		}
	}

	@Override
	public ServletConfig getServletConfig() {
		return this.servletConfig;
	}

	@Override
	public String[] getConfigLocations() {
		return super.getConfigLocations();
	}
	
	
}
