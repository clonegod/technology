package clonegod.spring.framework.context.support;


import java.io.IOException;

import clonegod.spring.framework.beans.factory.support.DefaultListableBeanFactory;
import clonegod.spring.framework.web.context.WebApplicationUtil;

public abstract class AbstractRefreshableApplicationContext  extends AbstractApplicationContext {
	
	/** Bean factory for this context */
	private DefaultListableBeanFactory beanFactory;

	/** Synchronization monitor for the internal BeanFactory */
	private final Object beanFactoryMonitor = new Object();
	
	
	/**
	 * This implementation performs an actual refresh of this context's underlying
	 * bean factory, shutting down the previous bean factory (if any) and
	 * initializing a fresh bean factory for the next phase of the context's lifecycle.
	 */
	@Override
	protected final void refreshBeanFactory() {
		// 关闭以存在的BeanFactory
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			// 创建新的BeanFactory
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			
			// 具体子类中提供实现：
			// 1、定位：加载配置文件，解析，生成BeanDefinition
			// 2、注册：将BeanDefinition注册到IOC容器中
			// 3、依赖注入：自动注入依赖的Bean
			loadBeanDefinitions(beanFactory);
			
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
	
	/**
	 * Create an internal bean factory for this context.
	 * 	默认使用DefaultListableBeanFactory作为BeanFactory
	 */
	protected DefaultListableBeanFactory createBeanFactory() {
		return new DefaultListableBeanFactory(WebApplicationUtil.getParentFactory());
	}
	
	protected void destroyBeans() {
		getBeanFactory().destroySingletons();
	}
	
	/**
	 * Determine whether this context currently holds a bean factory,
	 * i.e. has been refreshed at least once and not been closed yet.
	 */
	protected final boolean hasBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			return (this.beanFactory != null);
		}
	}

	@Override
	public final DefaultListableBeanFactory getBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory == null) {
				throw new IllegalStateException("BeanFactory not initialized or already closed - " +
						"call 'refresh' before accessing beans via the ApplicationContext");
			}
			return this.beanFactory;
		}
	}

	@Override
	protected final void closeBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			this.beanFactory = null;
		}
	}
	
	/**
	 * Load bean definitions into the given bean factory, typically through
	 * delegating to one or more bean definition readers.
	 * @param beanFactory the bean factory to load bean definitions into
	 * @throws BeansException if parsing of the bean definitions failed
	 * @throws IOException if loading of bean definition files failed
	 * @see org.springframework.beans.factory.support.PropertiesBeanDefinitionReader
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 */
	protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);
}
