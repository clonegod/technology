package clonegod.spring.framework.context;

import clonegod.spring.framework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 核心接口：refresh
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
	
	/**
	 * 初始化/刷新 IOC容器
	 */
	void refresh() throws Exception;
	
	
	DefaultListableBeanFactory getBeanFactory() throws IllegalStateException;
	
	
	
	/**
	 * Set the parent of this application context.
	 * <p>Note that the parent shouldn't be changed: It should only be set outside
	 * a constructor if it isn't available when an object of this class is created,
	 * for example in case of WebApplicationContext setup.
	 * @param parent the parent context
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext
	 */
	void setParent(ApplicationContext parent);
}
