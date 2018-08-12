# Sping MVC

	model:	封装数据的载体
	view:	显示数据的视图：HTML,JSP,XML,JSON...
	controller:	控制交互的中间组件，接收请求并调用相关服务，将结果封装到model中交给视图显示

## MVC模式的发展背景
最早，使用JSP页面进行开发，JSP的功能强大，页面中可以写html,css,js,java,sql等。

由于不同类型（职责）的代码都糅合在JSP中，代码耦合度高，非常难以维护和扩展。

MVC模式出现后，解决了页面与数据耦合的问题，将数据与页面进行了分离。

另外，Ajax应用的流行，也使得数据与页面分离应用得更加广泛。
	

## Spring MVC 的核心
	M: 
		模型，即封装数据的载体
		客户端发送的请求参数，spring自动封装为Object或Map 
		业务逻辑处理返回的结果，也可以封装为各种类型的Object
	V:
		视图，即显示数据的载体
		比如Thymeleaf模板引擎，编写静态html代码，再由模板引擎将Model中的数据填充到页面上
	C:
		控制器，即控制请求路由，调用业务逻辑，最后返回数据到视图进行呈现
	

## Spring MVC 特别需要搞清楚的几个点
	1、HandlerMapping	
		解析HTTP请求的URI，绑定到Controller中对应的handler上
		即，路由绑定

	2、HandlerAdpter		
		解析HTTP请求的参数，类型转换，并设置到handler的参数上
		即，参数绑定

	3、ViewResolver		
		将逻辑视图名称解析为真正的视图文件，将数据填充到视图上
		JSP：InternalResourceViewResolver
		Thymeleaf：ThymeleafViewResolver


## ApplicationContextAware
	
	public interface ApplicationContextAware extends Aware {

	/**
	 * 将应用上下文对象设置到子类中。
	 * 之后，子类就能直接通过ApplicaitonContext获取相关的Bean和Resource资源文件。
	 * /
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}

## DispatcherServlet 源码分析

####### FrameworkServlet extends HttpServletBean implements ApplicationContextAware 

	public class DispatcherServlet extends FrameworkServlet {

		/**
		 * This implementation calls {@link #initStrategies}.
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
			initMultipartResolver(context);	// 文件上传解析
			initLocaleResolver(context); // 国际化处理
			initThemeResolver(context); // 主题解析器
			
			* initHandlerMappings(context); // 将请求映射到handler
			* initHandlerAdapters(context); // handlerAdapter实现请求参数的动态绑定
			
			initHandlerExceptionResolvers(context); // 如果handler执行过程中发生异常，则交给HandlerExceptionResolver处理
			initRequestToViewNameTranslator(context); // 直接解析请求到视图名称
			
			* initViewResolvers(context); // 解析逻辑视图到具体视图
			
			initFlashMapManager(context); // flash属性管理器
		}

#
		/**
		 * 处理HTTP请求的具体逻辑
		 */
		@Override
		protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
			doDispatch(request, response);
		}
		
		// 对HTTP请求进行路由解析，参数绑定，调用handler，返回ModelAndView，响应结果
		protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
			HttpServletRequest processedRequest = request;
			HandlerExecutionChain mappedHandler = null;
			boolean multipartRequestParsed = false;
	
			try {
				ModelAndView mv = null;
				Exception dispatchException = null;
	
				try {
					processedRequest = checkMultipart(request);
					multipartRequestParsed = (processedRequest != request);
					
					// 1、getHandler 根据请求URI获取一个MappingHandler
					// Determine handler for the current request.
					mappedHandler = getHandler(processedRequest);
					if (mappedHandler == null || mappedHandler.getHandler() == null) {
						noHandlerFound(processedRequest, response);
						return;
					}
					
					// 2、根据handler的参数，将HTTP请求参数进行动态绑定
					// Determine handler adapter for the current request.
					HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
					
					// 3、handler执行之前，调用相关的Interceptor中的preHandle()
					if (!mappedHandler.applyPreHandle(processedRequest, response)) {
						return;
					}
					
					// 4、通过handlerAdapter，调用具体的handler处理，返回ModalAndView
					// Actually invoke the handler.
					mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
	
					if (asyncManager.isConcurrentHandlingStarted()) {
						return;
					}
					
					applyDefaultViewName(processedRequest, mv);
					
					// 5、handler执行结束后，调用相关的Interceptor中的postHandle()
					mappedHandler.applyPostHandle(processedRequest, response, mv);
				}
				catch (Exception ex) {
				}
				catch (Throwable err) {
				}
				
				// 6、将ModelAndView交给ViewResolver进行视图解析
				processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
			}
			catch (Exception ex) {
				// 7、如果发生异常，调用Interceptor的afterCompletion，把异常传递过去
				triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
			}
			finally {
			}
		}

	}


## HandlerInterceptor 接口

	public interface HandlerInterceptor {

		/**
		 * 前置拦截，该方法在handlerAdpater调用handler之前执行。
		 * interceptors可以是多个，这些interceptors会按配置顺序依次执行。
		 * 
		 * 如果其中某个interceptor返回false，则该HTTP请求会立即完成响应，
		 * 不再继续往下执行，而且handler也不会被调用到。
		 */
		boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		    throws Exception;
	
		/**
		 * 后置拦截，该方法在handlerAdapter调用handler之后执行，
		 * 但是，在DispatchServlet调用render()进行视图处理器之前执行。
		 * 
		 * interceptors可以是多个，这些interceptors会按配置顺序倒序执行。
		 */
		void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
				throws Exception;
	
		/**
		 * 在HTTP请求完整处理结束之后才被调用，即在render view已经结束。
		 * 
		 * 只有该interceptor的preHandler返回true，该方法才可能被调用。
		 * 
		 * interceptors可以是多个，这些interceptors会按配置顺序倒序执行。
		 */
		void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
				throws Exception;
	
	}

	