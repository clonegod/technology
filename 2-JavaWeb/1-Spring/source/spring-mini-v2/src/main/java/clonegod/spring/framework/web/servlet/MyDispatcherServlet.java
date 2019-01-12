package clonegod.spring.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clonegod.spring.framework.web.context.WebApplicationContext;

/**
 * DispatchServlet要做两件事：
 * 	1、初始化springMVC相关的组件--初始化 Child WebApplicationContext，
 *  2、处理HTTP请求，转发到对应的handler处理，返回ModelAndView 
 */
public class MyDispatcherServlet extends FrameworkServlet {
	
	/**
	 * Servlet的init()方法被调用，进而触发DispatchServlet初始化WEB相关的组件
	 */
	@Override
	protected void onRefresh(WebApplicationContext context) {
		initStrategies(context);
	}
	
	/**
	 * Initialize the strategy objects that this servlet uses.
	 * <p>May be overridden in subclasses in order to initialize further strategy objects.
	 */
	protected void initStrategies(WebApplicationContext context) {
//		initMultipartResolver(context);
//		initLocaleResolver(context);
//		initThemeResolver(context);
		initHandlerMappings(context);
		initHandlerAdapters(context);
//		initHandlerExceptionResolvers(context);
//		initRequestToViewNameTranslator(context);
		initViewResolvers(context);
//		initFlashMapManager(context);
	}


	private void initHandlerMappings(WebApplicationContext context) {
		System.out.println("initHandlerMappings ...");
	}

	private void initHandlerAdapters(WebApplicationContext context) {
		System.out.println("initHandlerAdapters ...");
		
	}

	private void initViewResolvers(WebApplicationContext context) {
		System.out.println("initViewResolvers ...");
		
	}

	//===========================================================================//
	
	/**
	 * 处理HTTP请求
	 * @param request
	 * @param response
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			System.out.println("doService invoke doDispatch ...");
			doDispatch(request, response);
		}
		finally {
		}
	}

	/**
	 * 提取请求URI，从HandlerMapping找到与URI匹配的handler，
	 * handlerAdapter将请求参数绑定到handler上，通过反射调用此handler
	 * 返回ModelAndView，交给viewResolver处理，响应结果
	 * 
	 * @param request
	 * @param response
	 */
	private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
//		HttpServletRequest processedRequest = request;
//
//		try {
//			ModelAndView mv = null;
//			Exception dispatchException = null;
//
//			try {
//				processedRequest = checkMultipart(request);
//				multipartRequestParsed = (processedRequest != request);
//
//				// Determine handler for the current request.
//				mappedHandler = getHandler(processedRequest);
//				if (mappedHandler == null || mappedHandler.getHandler() == null) {
//					noHandlerFound(processedRequest, response);
//					return;
//				}
//
//				// Determine handler adapter for the current request.
//				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
//
//				// Process last-modified header, if supported by the handler.
//				String method = request.getMethod();
//				boolean isGet = "GET".equals(method);
//				if (isGet || "HEAD".equals(method)) {
//					long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
//					if (logger.isDebugEnabled()) {
//						logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
//					}
//					if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
//						return;
//					}
//				}
//
//				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
//					return;
//				}
//
//				// Actually invoke the handler.
//				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
//
//				if (asyncManager.isConcurrentHandlingStarted()) {
//					return;
//				}
//
//				applyDefaultViewName(processedRequest, mv);
//				mappedHandler.applyPostHandle(processedRequest, response, mv);
//			}
//			catch (Exception ex) {
//				dispatchException = ex;
//			}
//			catch (Throwable err) {
//			}
//			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		catch (Throwable err) {
//			err.printStackTrace();
//		}
//		finally {
//			
//		}
	}
	

	
}
