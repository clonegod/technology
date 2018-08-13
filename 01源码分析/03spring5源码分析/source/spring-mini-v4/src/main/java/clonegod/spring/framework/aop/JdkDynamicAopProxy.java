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

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 判断当前被调用的方法，是否配置了切面
		
		// 前置
		if(aopConfig != null && this.aopConfig.containsAspect(method)) {
			AspectWrapper aspect =  this.aopConfig.getAspcet(method);
			aspect.getMethods()[0].invoke(aspect.getAspect());
		}
		
		Object result = method.invoke(this.targetInstance, args);
		
		// 后置
		if(aopConfig != null && this.aopConfig.containsAspect(method)) {
			AspectWrapper aspect =  this.aopConfig.getAspcet(method);
			aspect.getMethods()[1].invoke(aspect.getAspect());
		}
		
		return result;
	}

}