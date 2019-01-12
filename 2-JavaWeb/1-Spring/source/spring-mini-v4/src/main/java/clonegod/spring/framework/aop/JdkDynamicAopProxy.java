package clonegod.spring.framework.aop;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import clonegod.spring.framework.aop.AopConfig.AspectWrapper;

public final class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable {
	
	private Object targetInstance;
	private AopConfig aopConfig;
	
	public void setAopConfig(AopConfig aopConfig) {
		this.aopConfig = aopConfig;
	}
	
	public JdkDynamicAopProxy(Object targetInstance) {
		this.targetInstance = targetInstance;
	}

	@Override
	public Object getProxy() {
		Class<?> clazz = this.targetInstance.getClass();
		return Proxy.newProxyInstance(clazz.getClassLoader(), 
									clazz.getInterfaces(), 
									this);
	}

	/**
	 * spring 注入的是 代理对象，代理对象的方法被调用时会回调invoke()，
	 * 在这里就可以对拦截到的方法进行各种操作：事务控制，日志记录，安全控制。。。
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 判断当前被调用的方法，是否配置了切面
		
		// 前置
		if(aopConfig != null && this.aopConfig.containsAspect(method)) {
			AspectWrapper aspect =  this.aopConfig.getAspcet(method);
			aspect.getMethods()[0].invoke(aspect.getAspect());
		}
		
		Object result = method.invoke(this.targetInstance, args); // 调用被代理对象的方法
		
		// 后置
		if(aopConfig != null && this.aopConfig.containsAspect(method)) {
			AspectWrapper aspect =  this.aopConfig.getAspcet(method);
			aspect.getMethods()[1].invoke(aspect.getAspect());
		}
		
		return result;
	}

}