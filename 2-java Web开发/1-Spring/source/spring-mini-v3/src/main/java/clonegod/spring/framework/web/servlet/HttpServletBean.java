package clonegod.spring.framework.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class HttpServletBean extends HttpServlet {

	protected ServletConfig config;
	
	/**
	 * Map config parameters onto bean properties of this servlet, and
	 * invoke subclass initialization.
	 * @throws ServletException if bean properties are invalid (or required
	 * properties are missing), or if subclass initialization fails.
	 */
	@Override
	public final void init() throws ServletException {
		System.out.println("\n\n[start]HttpServletBean init...........");
		
		this.config = getServletConfig();
		// Set bean properties from init parameters.
//		try {
//			PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
//			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
//			ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
//			bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
//			initBeanWrapper(bw);
//			bw.setPropertyValues(pvs, true);
//		}
//		catch (BeansException ex) {
//			logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
//			throw ex;
//		}

		// Let subclasses do whatever initialization they like.
		initServletBean();

	}
	
	/**
	 * Subclasses may override this to perform custom initialization.
	 * All bean properties of this servlet will have been set before this
	 * method is invoked.
	 * <p>This default implementation is empty.
	 * @throws ServletException if subclass initialization fails
	 */
	protected void initServletBean() throws ServletException {
	}
	
}
