package clonegod.spring.framework.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clonegod.spring.framework.annotation.Controller;
import clonegod.spring.framework.annotation.RequestMapping;
import clonegod.spring.framework.annotation.RequestParam;
import clonegod.spring.framework.web.context.WebApplicationContext;
import clonegod.spring.framework.web.servlet.mvc.HandlerAdapter;
import clonegod.spring.framework.web.servlet.mvc.HandlerMapping;
import clonegod.spring.framework.web.servlet.mvc.ModelAndView;
import clonegod.spring.framework.web.servlet.mvc.ViewResolver;

/**
 * DispatchServlet要做两件事：
 * 	1、初始化springMVC相关的组件--初始化 Child WebApplicationContext，
 *  2、处理HTTP请求，转发到对应的handler处理，返回ModelAndView 
 */
public class MyDispatcherServlet extends FrameworkServlet {
	
	/** List of HandlerMappings used by this servlet */
	private List<HandlerMapping> handlerMappings;

	/** List of HandlerAdapters used by this servlet */
	private List<HandlerAdapter> handlerAdapters;
	
	/** List of ViewResolvers used by this servlet */
	private List<ViewResolver> viewResolvers;
	
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
//		initMultipartResolver(context); // 文件上传
//		initLocaleResolver(context); // 国际化
//		initThemeResolver(context); // 主题
		initHandlerMappings(context); /** -> 初始化RequestMapping与method的对应关系 */ 
		initHandlerAdapters(context); /** -> 从httpRequest中获取参数，完成类型转换，调用handler时，绑定到handler的参数上 */
//		initHandlerExceptionResolvers(context); // 异常处理
//		initRequestToViewNameTranslator(context); // 直接根据请求转换得到视图名称
		initViewResolvers(context); /** -> 通过ViewResolver完成视图解析 */
//		initFlashMapManager(context); // flash属性管理
	}

	/**
	 * 找到所有Controller，解析RequestMapping中的url，与method建立关联
	 */
	private void initHandlerMappings(WebApplicationContext context) {
		System.out.println("initHandlerMappings ...");
		this.handlerMappings = new ArrayList<HandlerMapping>();
		
		Map<String, Object> controllerMap = context.getBeansWithAnnotation(Controller.class);
		System.out.println(controllerMap);
		
		for(Entry<String, Object> entry : controllerMap.entrySet()) {
			Object controller = entry.getValue();
			Class<?> controllerClass = controller.getClass();
			String baseUrl = "";
			if(controllerClass.isAnnotationPresent(RequestMapping.class)) {
				baseUrl = controllerClass.getAnnotation(RequestMapping.class).value();
			}
			
			// 扫描所有的public method
			for(Method m : controllerClass.getDeclaredMethods()) {
				System.out.println(m);
				if(! m.isAnnotationPresent(RequestMapping.class)) {
					continue;
				}
				RequestMapping reqMapping = m.getAnnotation(RequestMapping.class);
				String urlPattern = ("/" + baseUrl + reqMapping.value()).replaceAll("/+", "/");
				HandlerMapping hm = new HandlerMapping(Pattern.compile(urlPattern), controller, m);
				this.handlerMappings.add(hm);
			}
		}
		System.out.println(handlerMappings);
	}

	/**
	 * 记录普通参数，命名参数的位置，为反射调用handler时传递参数做准备
	 * 
	 */
	private void initHandlerAdapters(WebApplicationContext context) {
		System.out.println("initHandlerAdapters ...");
		this.handlerAdapters = new ArrayList<>();
		
		Map<String, Integer> paramIndexMap = new HashMap<>();
		
		for(HandlerMapping hm : this.handlerMappings) {
			// 解析@RequestParam的参数
			Annotation[][] annotations = hm.getHandler().getParameterAnnotations();
			for(int index = 0; index < annotations.length; index++) {
				for(Annotation anno0 : annotations[index]) {
					if(anno0 instanceof RequestParam) {
						String paramName = ((RequestParam) anno0).value();
						paramIndexMap.put(paramName, index);
					}
				}
			}
			
			// 解析特殊内置参数的位置
			Class<?>[] paramTypes = hm.getHandler().getParameterTypes();
			for(int index = 0; index < paramTypes.length; index++)  {
				Class<?> paramType = paramTypes[index];
				if(paramType == HttpServletRequest.class || paramType == HttpServletResponse.class) {
					paramIndexMap.put(paramType.getName(), index);
				}
			}
			this.handlerAdapters.add(new HandlerAdapter(hm, paramIndexMap));
		}
	}

	/**
	 * 根据逻辑视图名称，找到对应视图文件
	 * 
	 * @param context
	 */
	private void initViewResolvers(WebApplicationContext context) {
		System.out.println("initViewResolvers ...");
		try {
			this.viewResolvers = new ArrayList<>();
			
			Properties prop = new Properties();
			URL url = this.getClass().getResource("/" + getContextConfigLocation().replace("classpath:", ""));
			prop.load(url.openStream());
			
			String templateRoot = prop.getProperty("template-path");
			
			File file = new File(this.getClass().getResource("/" + templateRoot).toURI());
			Arrays.asList(file.listFiles())
					.stream()
					.forEach(f -> viewResolvers.add(new ViewResolver(f.getName(), Paths.get(f.toURI()), ".html")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerExecutionChain mappedHandler = null;

		try {
			ModelAndView mv = null;
			Exception dispatchException = null;

			try {
				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest);
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}

				// Determine handler adapter for the current request.
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

				if (!mappedHandler.applyPreHandle(processedRequest, response)) {
					return;
				}
				
				// Actually invoke the handler.
				mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

				mappedHandler.applyPostHandle(processedRequest, response, mv);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				dispatchException = ex;
			}
			
			// View Resolver
			processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		}
		catch (Exception ex) {
			triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
		}
		catch (Throwable err) {
			triggerAfterCompletion(processedRequest, response, mappedHandler,
					new RuntimeException("Handler processing failed", err));
		}
		finally {
		}
	}
	
	/**
	 * 根据请求，得到一个匹配的handler 
	 */
	private HandlerExecutionChain getHandler(HttpServletRequest request) {
		String url = request.getRequestURI().replaceAll(request.getServletContext().getContextPath(), "").replaceAll("/+", "/");
		HandlerExecutionChain handler =  null;
		for(HandlerMapping hm : this.handlerMappings) {
			if(hm.getUrlPattern().matcher(url).matches()) {
				handler = new HandlerExecutionChain(hm.getControllerInstance());
				break;
			}
		}
		return handler;
	}
	
	/**
	 * 请求错误，没有handler与之匹配
	 */
	private void noHandlerFound(HttpServletRequest processedRequest, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	/**
	 * 参数获取，类型转换
	 */
	private HandlerAdapter getHandlerAdapter(Object handler) {
		for(HandlerAdapter ha : this.handlerAdapters) {
			Object controller = ha.getHandlerMapping().getControllerInstance();
			if(controller.getClass() == handler.getClass()) {
				return ha;
			}
		}
		
		return null;
	}
	
	/**
	 * 调用ViewResolver生成响应视图
	 */
	private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
			HandlerExecutionChain mappedHandler, ModelAndView mv, Exception dispatchException) throws Exception {
		System.out.println("@@@@ view render 被调用");
		if(mv == null) {
			return;
		}
		
		render(mv, request, response);
		
		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, dispatchException);
		}
	}


	private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ViewResolver viewResolve = null;
		for(ViewResolver resolver : this.viewResolvers) {
			if(Objects.equals(mv.getView() + resolver.getSuffix(), resolver.getViewName())) {
				viewResolve = resolver;
				break;
			}
		}
		
		StringBuilder buf = new StringBuilder();
		
		File file = viewResolve.getViewPath().toFile();
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		
		final Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
		
		String line;
		while((line = raf.readLine()) != null) {
			String utf8Line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
			Matcher m = p.matcher(line);
			while(m.find()) {
				String key = m.group(1);
				String value = (String) mv.getModel().get(key);
				utf8Line = utf8Line.replaceAll("\\$\\{" + key + "\\}", value);
			}
			buf.append(utf8Line);
		}
		raf.close();
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(buf.toString());
	}

	private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response,
			HandlerExecutionChain mappedHandler, Exception ex) throws Exception {

		if (mappedHandler != null) {
			mappedHandler.triggerAfterCompletion(request, response, ex);
		}
		throw ex;
	}
}
