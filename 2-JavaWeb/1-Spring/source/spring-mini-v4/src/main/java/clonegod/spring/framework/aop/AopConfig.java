package clonegod.spring.framework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 目标方法被调用时，执行切面对象中的Before,After,Around方法
 * 
 */
public class AopConfig {
	
	private Map<Method, AspectWrapper> aspectMapping = new HashMap<>();
	
	public void addAspcet(Method method, Object aspect, Method ...methods) {
		AspectWrapper aspectWrapper = new AspectWrapper(aspect, methods); 
		this.aspectMapping.put(method, aspectWrapper);
		// 兼容接口方法
		try {
			this.aspectMapping.put(
					method.getDeclaringClass().getInterfaces()[0].getMethod(method.getName(), method.getParameterTypes()), 
					aspectWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AspectWrapper getAspcet(Method method) {
		return this.aspectMapping.get(method);
	}
	
	public boolean containsAspect(Method method) {
		return this.aspectMapping.containsKey(method);
	}

	public static class AspectWrapper {
		private Object aspect; // 切面对象
		private Method[] methods; // 切面方法
		
		public AspectWrapper(Object aspect, Method[] methods) {
			super();
			this.aspect = aspect; // LogAspect切面对象
			this.methods = methods; // LogAsppect的before,after方法
		}

		public Object getAspect() {
			return aspect;
		}

		public Method[] getMethods() {
			return methods;
		}
	}
	
}
