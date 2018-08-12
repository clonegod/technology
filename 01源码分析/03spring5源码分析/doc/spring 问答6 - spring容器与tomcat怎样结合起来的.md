# spring 与 tomcat

	web应用部署到tomcat中，通过部署描述符文件web.xml声明相关的配置。

	其中，对spring而言，要配置两个地方：
		配置ContextLoaderListener，用来接收Servlet容器（即Tomcat）的启动事件，停止事件
			当tomcat启动时，会回调contextInitialized()，spring在这里开始初始化IOC容器
			当tomcat停止时，会回调contextDestroyed()，spring在这里关闭IOC容器

		配置DispatchServlet
			将客户端的HTTP请求转发到对应的Controller的handler进行处理，
			处理后结果，交给ViewResolver进行处理，
			返回结果可能是一个html页面，也可能是JSON，XML，PDF等各种视图。


# web.xml
	<!-- 配置ApplicationContext，初始化业务层、数据层的Bean  -->
	<context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/business-config.xml</param-value>
    </context-param>
	
	<listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

	<!-- 配置WebApplicationContext，初始化与WEB相关的Bean  -->
	<servlet>
		<servlet-name>spring4</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/mvc-core-config.xml, classpath:spring/tools-config.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>spring4</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>


---

## ServletContextListener	通知容器启动/停止事件
	// servlet规范：实现ServletContextListener接口的类，在容器初始化时会被调用
	public interface ServletContextListener extends EventListener {
	
	    /**
	     * 容器(Tomcat)启动时，发出初始化事件。
	     *
	     */
	    public void contextInitialized(ServletContextEvent sce);
	
	    /**
	     * 容器(Tomcat)停止时，发出销毁事件。
	     */
	    public void contextDestroyed(ServletContextEvent sce);
	}

## ContextLoader  加载配置，启动spring 容器
ContextLoader中提供了加载Spring MVC相关配置文件的功能

	public class ContextLoader {

		public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

		public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
			long startTime = System.currentTimeMillis();
	
			try {
				// Store context in local instance variable, to guarantee that
				// it is available on ServletContext shutdown.
				if (this.context == null) {
					this.context = createWebApplicationContext(servletContext);
				}
				if (this.context instanceof ConfigurableWebApplicationContext) {
					ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.context;
					if (!cwac.isActive()) {
						// The context has not yet been refreshed -> provide services such as
						// setting the parent context, setting the application context id, etc
						if (cwac.getParent() == null) {
							// The context instance was injected without an explicit parent ->
							// determine parent for root web application context, if any.
							ApplicationContext parent = loadParentContext(servletContext);
							cwac.setParent(parent);
						}
						// 启动容器 - refresh
						configureAndRefreshWebApplicationContext(cwac, servletContext);
					}
				}
				
				return this.context;
			}
			catch (RuntimeException ex) {
			}
			catch (Error err) {
			}
		}

		// 加载配置文件、启动ioc容器
		protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {	
			wac.setServletContext(sc);
			// 获取web.xml中配置的spring配置文件的路径
			String configLocationParam = sc.getInitParameter(CONFIG_LOCATION_PARAM);

			if (configLocationParam != null) {
				wac.setConfigLocation(configLocationParam);
			}

			// 调用AbstractApplicationContext的refresh()开始初始化IOC容器
			wac.refresh();
		}
	
	}

---
~~~~~~~~~~~~~~~~~~~~~
---
	
## ContextLoaderListener
接收tomcat容器的启动事件，是spring启动入口

两个关键点：

	1、 ContextLoaderListener extends ContextLoader
		继承ContextLoader，ContextLoader中提供了初始化容器的入口。
		
	2、 ContextLoaderListener implements ServletContextListener 
		Servlet容器启动时，将得到通知，于是触发spring 容器的初始化。

#
	public class ContextLoaderListener extends ContextLoader implements ServletContextListener {

		/**
		 * Create a new {@code ContextLoaderListener} that will create a web application
		 * context based on the "contextClass" and "contextConfigLocation" servlet
		 * context-params. 
		 */
		public ContextLoaderListener() {
		}
	
		public ContextLoaderListener(WebApplicationContext context) {
			super(context);
		}
	
		/**
		 * Initialize the root web application context.
		 */
		@Override
		public void contextInitialized(ServletContextEvent event) {
			// 调用父类ContextLoader的initWebApplicationContext()
			initWebApplicationContext(event.getServletContext());
		}
	
	
		/**
		 * Close the root web application context.
		 */
		@Override
		public void contextDestroyed(ServletContextEvent event) {
			closeWebApplicationContext(event.getServletContext());
			ContextCleanupListener.cleanupAttributes(event.getServletContext());
		}
	
	}

---
~~~~~~~~~~~~~~~~~~~~~
---

## 首先，DispatcherServlet需要初始化
	>HttpServlet.init()
	>HttpServletBean.init()
	>FrameworkServlet.initServletBean()
	>DispatchServlet.onRefresh()
	>DispatchServlet.initStrategies()

## 然后，DispatcherServlet处理HTTP请求
	>HttpServlet.doGet()/doPost()/...
	>FrameworkServlet.doGet()/doPost()/...
	>FrameworkServlet.processRequest()
	>FrameworkServlet.doService()
	>DispatcherServlet.doService()
	>DispatcherServlet.doDispatch()
	

## HttpServletBean
public abstract class HttpServletBean extends HttpServlet implements EnvironmentCapable, EnvironmentAware 

	// Servlet被容器实例化之后，init()被调用，进行初始化
	@Override
	public final void init() throws ServletException {
		if (logger.isDebugEnabled()) {
			logger.debug("Initializing servlet '" + getServletName() + "'");
		}

		// Set bean properties from init parameters.
		try {
			PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
			ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
			bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
			initBeanWrapper(bw);
			bw.setPropertyValues(pvs, true);
		}
		catch (BeansException ex) {
			logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
			throw ex;
		}
		
		// 初始化Servlet
		// Let subclasses do whatever initialization they like.
		initServletBean();

		if (logger.isDebugEnabled()) {
			logger.debug("Servlet '" + getServletName() + "' configured successfully");
		}
	}


## FrameworkServlet
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware 

	/**
	 * Overridden method of {@link HttpServletBean}, invoked after any bean properties
	 * have been set. 
	 * Creates this servlet's WebApplicationContext.
	 */
	@Override
	protected final void initServletBean() throws ServletException {
		getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
		if (this.logger.isInfoEnabled()) {
			this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
		}
		long startTime = System.currentTimeMillis();

		try {
			// 初始化WebApplicationContext，具体逻辑
			this.webApplicationContext = initWebApplicationContext();
			initFrameworkServlet();
		}
		catch (RuntimeException ex) {
		}
		
		// 打印日志：WebApplicationContext初始化完成
		if (this.logger.isInfoEnabled()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " +
					elapsedTime + " ms");
		}
	}

	protected WebApplicationContext initWebApplicationContext() {
		WebApplicationContext rootContext =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		WebApplicationContext wac = null;

		if (!this.refreshEventReceived) {
			// 由子类DispatchServlet的onRefresh()进行具体实现
			onRefresh(wac);
		}

		return wac;
	}

## DispatchServlet
public class DispatcherServlet extends FrameworkServlet 

##### DispatchServlet 内部最重要的3个组件：
	HandlerMapping: 维护请求URI和Controller中的handler的映射关系
	HandlerAdapter: 找到与URI匹配的handler，并进行调用，调用时会自动处理参数绑定的问题
	ViewResolver：接收handler返回的结果，将数据进行填充

# 1、DispatchServlet 首先初始化MVC相关组件
	/**
	 * This implementation calls {@link #initStrategies}.
	 * onRefresh()方法在FrameworkServlet的initWebApplicationContext()中进行调用
	 */
	@Override
	protected void onRefresh(ApplicationContext context) {
		initStrategies(context);
	}

	/**
	 * Initialize the strategy objects that this servlet uses.
	 * <p>May be overridden in subclasses in order to initialize further strategy objects.
	 */
	protected void initStrategies(ApplicationContext context) {
		// 初始化MVC相关的组件，比如HandlerMapping,HandlerAdapter,ViewResolver等
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);
		initHandlerAdapters(context);
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);
		initFlashMapManager(context);
	}


### 2、DispatchServlet 处理HTTP请求
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			doDispatch(request, response);
		}
		finally {
			
		}
	}

	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;
		boolean multipartRequestParsed = false;

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

		try {
			ModelAndView mv = null;
			Exception dispatchException = null;

			try {
				processedRequest = checkMultipart(request);
				multipartRequestParsed = (processedRequest != request);

				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

				// Process last-modified header, if supported by the handler.
				String method = request.getMethod();
				boolean isGet = "GET".equals(method);
				if (isGet || "HEAD".equals(method)) {
					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
					if (logger.isDebugEnabled()) {
						logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
					}
					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
						return;
					}
				}

				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}

				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
		}
		catch (Throwable err) {
		}
		finally {
		}
	}