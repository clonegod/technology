package clonegod.spring.framework.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

public class AopProxyUtils {
	
	/**
	 * 从代理对象中找到原始的target对象
	 */
	public static Object getTargetClass(Object object) {
		
		if(! isAopProxy(object)) {
			return object;
		}
		
		return getProxyTargetObject(object);
		
	}
	
	private static boolean isAopProxy(Object object) {
		return Proxy.isProxyClass(object.getClass());
	}
	
	private static Object getProxyTargetObject(Object object) {
		try {
			// $Proxy8 中的 h 字段: InvocationHandler的实现类
			Field h = object.getClass().getSuperclass().getDeclaredField("h");
			h.setAccessible(true);
			JdkDynamicAopProxy proxy = (JdkDynamicAopProxy) h.get(object);
			Field target = proxy.getClass().getDeclaredField("targetInstance");
			target.setAccessible(true);
			return target.get(proxy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
