package clonegod.spring.framework.beans;

import clonegod.spring.framework.aop.AopConfig;
import clonegod.spring.framework.aop.AopProxy;
import clonegod.spring.framework.aop.JdkDynamicAopProxy;

public class BeanWrapperImpl implements BeanWrapper {
	
	private Object originalObject; // 原始的Bean实例
	private Object proxyObject; // 原始Bean对象的代理对象
	private AopProxy aopProxy;
	
	public BeanWrapperImpl(Object instance) {
		this.originalObject = instance;
		this.aopProxy = new JdkDynamicAopProxy(instance);
		this.proxyObject = aopProxy.getProxy();
	}
	
	public Object getOriginalObject() {
		return originalObject;
	}

	/**
	 * 返回原始Bean的代理对象
	 */
	public final Object getWrappedInstance() {
		return this.proxyObject;
	}

	public final Class<?> getWrappedClass() {
		return (this.proxyObject != null ? this.proxyObject.getClass() : null);
	}
	
	public void setAopConfig(AopConfig aopConfig) {
		((JdkDynamicAopProxy)aopProxy).setAopConfig(aopConfig);
	}
}
