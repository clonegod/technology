# spring 与 tomcat

	web应用部署到tomcat中，通过部署描述符文件web.xml声明相关的配置。

	其中，对spring而言，要配置两个地方：
		配置ContextLoaderListener，用来接收Servlet容器（即Tomcat）的启动事件，停止事件
			当tomcat启动时，会回调contextInitialized()，spring在这里开始初始化IOC容器
			当tomcat停止时，会回调contextDestroyed()，spring在这里关闭IOC容器

		配置DispatchServlet，用来接收客户端的HTTP请求。


## 配置ContextLoaderListener
	
	public interface ServletContextListener extends EventListener {
	
	    /**
	     * Receives notification that the web application initialization
	     * process is starting.
	     *
	     * <p>All ServletContextListeners are notified of context
	     * initialization before any filters or servlets in the web
	     * application are initialized.
	     *
	     * @param sce the ServletContextEvent containing the ServletContext
	     * that is being initialized
	     */
	    public void contextInitialized(ServletContextEvent sce);
	
	    /**
	     * Receives notification that the ServletContext is about to be
	     * shut down.
	     *
	     * <p>All servlets and filters will have been destroyed before any
	     * ServletContextListeners are notified of context
	     * destruction.
	     *
	     * @param sce the ServletContextEvent containing the ServletContext
	     * that is being destroyed
	     */
	    public void contextDestroyed(ServletContextEvent sce);
	}

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