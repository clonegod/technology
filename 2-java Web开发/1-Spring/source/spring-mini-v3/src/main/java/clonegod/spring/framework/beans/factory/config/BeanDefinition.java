package clonegod.spring.framework.beans.factory.config;

/**
 * 封装Bean的相关属性
 *
 */
public class BeanDefinition {
	
	private String beanName;
	
	private Class<?> beanClass;

	private boolean lazyInit = false;
	
	private boolean singleton = true;
	
	private boolean prototype = false;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isPrototype() {
		return prototype;
	}

	public void setPrototype(boolean prototype) {
		this.prototype = prototype;
	}

}
