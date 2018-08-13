package clonegod.spring.framework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import clonegod.spring.framework.context.ConfigurableApplicationContext;

/**
 * ConfigurableWebApplicationContext 
 *	Root Context 和  Child Context， 它们的 ConfigLocations 是不同的
 *	因此会创建出两个ConfigurableWebApplicationContext，一个作为root，一个作为child！
 *	
 */
public interface ConfigurableWebApplicationContext extends WebApplicationContext , ConfigurableApplicationContext {
	void setServletContext(ServletContext servletContext);

	/**
	 * Set the ServletConfig for this web application context.
	 * Only called for a WebApplicationContext that belongs to a specific Servlet.
	 * @see #refresh()
	 */
	void setServletConfig(ServletConfig servletConfig);

	/**
	 * Return the ServletConfig for this web application context, if any.
	 */
	ServletConfig getServletConfig();

	/**
	 * Set the config locations for this web application context in init-param style,
	 * i.e. with distinct locations separated by commas, semicolons or whitespace.
	 * <p>If not set, the implementation is supposed to use a default for the
	 * given namespace or the root web application context, as appropriate.
	 */
    void setConfigLocation(String configLocation);

	/**
	 * Set the config locations for this web application context.
	 * <p>If not set, the implementation is supposed to use a default for the
	 * given namespace or the root web application context, as appropriate.
	 */
	void setConfigLocations(String... configLocations);

	/**
	 * Return the config locations for this web application context,
	 * or {@code null} if none specified.
	 */
	String[] getConfigLocations();
	
}
