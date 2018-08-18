package clonegod.spring.framework.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clonegod.spring.framework.context.ApplicationContext;
import clonegod.spring.framework.web.context.ConfigurableWebApplicationContext;
import clonegod.spring.framework.web.context.WebApplicationContext;
import clonegod.spring.framework.web.context.support.MySimpleWebApplicationContext;

public abstract class FrameworkServlet extends HttpServletBean {
	
	public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
	
	/** Explicit context config location */
	private String contextConfigLocation;
	
	/** WebApplicationContext for this servlet */
	private WebApplicationContext webApplicationContext;
	
	public void setContextConfigLocation(String contextConfigLocation) {
		this.contextConfigLocation = contextConfigLocation;
	}
	
	public String getContextConfigLocation() {
		return this.contextConfigLocation;
	}
	
	public final WebApplicationContext getWebApplicationContext() {
		return this.webApplicationContext;
	}
	
	/**
	 * 为DispatchServlet初始化一个专用的 WebApplicationContext
	 */
	@Override
	protected final void initServletBean() throws ServletException {
		try {
			this.setContextConfigLocation(super.config.getInitParameter(CONFIG_LOCATION_PARAM));
			this.webApplicationContext = initWebApplicationContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WebApplicationContext initWebApplicationContext() throws Exception {
		WebApplicationContext rootContext = null;
		WebApplicationContext wac = createWebApplicationContext(rootContext);
		
		// 触发DispatchServlet初始化MVC组件
		onRefresh(wac);
		
		return wac;
	}

	protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) throws Exception {
		ConfigurableWebApplicationContext wac = new MySimpleWebApplicationContext();
		wac.setParent(parent);
		wac.setConfigLocation(getContextConfigLocation());
		
//			wac.setServletContext(getServletContext());
		wac.setServletConfig(getServletConfig());
		wac.refresh();
		
		return wac;
	}
	
	/**
	 * Template method which can be overridden to add servlet-specific refresh work.
	 * Called after successful context refresh.
	 * <p>This implementation is empty.
	 * @param context the current WebApplicationContext
	 * @see #refresh()
	 */
	protected void onRefresh(WebApplicationContext context) {
		// For subclasses: do nothing by default.
		// --- DispatchServlet 复写此方法，初始化MVC组件
	}
	
	
	/**
	 * 处理HTTP请求
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			doService(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Successfully completed request");
		}
		
	}
	
	protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
			throws Exception;
	
}
