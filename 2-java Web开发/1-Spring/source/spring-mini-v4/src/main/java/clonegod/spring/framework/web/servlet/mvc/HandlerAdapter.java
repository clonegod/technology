package clonegod.spring.framework.web.servlet.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapter {

	private HandlerMapping hm;
	
	private Map<String, Integer> paramIndexs;

	public HandlerAdapter(HandlerMapping hm, Map<String, Integer> paramIndexMap) {
		this.hm = hm;
		this.paramIndexs = paramIndexMap;
	}
	
	public HandlerMapping getHandlerMapping() {
		return hm;
	}

	/**
	 * 调用handler，返回ModelAndView
	 */
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		System.out.println("@@@@ handle 被调用");
		
		// 方法的参数类型
		Class<?>[] paramTypes = this.hm.getHandler().getParameterTypes();
		
		// 方法的参数列表
		Object[] args = new Object[paramTypes.length];
		
		// 处理http请求参数
		Map<String, String[]> params = request.getParameterMap();
		for(String name : params.keySet()) {
			String value = params.get(name)[0];
			if(this.paramIndexs.containsKey(name)) {
				int index = this.paramIndexs.get(name);
				// 根据参数的位置，获取对应参数的类型，进行自动类型转换
				args[index] = convertValueType(value, paramTypes[index]);
			}
		}
		
		// 处理特殊参数
		int reqIndex = this.paramIndexs.get(HttpServletRequest.class.getName());
		args[reqIndex] = request;
		
		int resIndex = this.paramIndexs.get(HttpServletResponse.class.getName());
		args[resIndex] = response;
		
		Object result = hm.getHandler().invoke(controller, args);
		
		if(result == null) {
			return null;
		}
		
		if(result instanceof ModelAndView) {
			return (ModelAndView) result;
		} else {
			response.getWriter().write(result.toString());
		}
		
		return null;
	}

	private Object convertValueType(String value, Class<?> class1) {
		if(class1 == String.class) {
			return value;
		} else if (class1 == Integer.class || class1 == int.class) {
			return Integer.parseInt(value);
		} else {
			throw new RuntimeException("仅支持Integer/int类型自动转换，其它类型未实现！");
		}
	}

}
