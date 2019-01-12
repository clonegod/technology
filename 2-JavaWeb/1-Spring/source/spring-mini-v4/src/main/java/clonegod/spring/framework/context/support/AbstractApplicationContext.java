package clonegod.spring.framework.context.support;

import java.lang.annotation.Annotation;
import java.util.Map;

import clonegod.controller.SimpleController;
import clonegod.spring.framework.beans.factory.BeanFactory;
import clonegod.spring.framework.beans.factory.support.DefaultListableBeanFactory;
import clonegod.spring.framework.context.ApplicationContext;
import clonegod.spring.framework.context.ConfigurableApplicationContext;

public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {
	/** Parent context */
	private ApplicationContext parent;
	
	private final Object startupShutdownMonitor = new Object();
	
	/** Display name */
	private String displayName = this.getClass().getSimpleName();

	private long startupDate;
	
	//---------------------------------------------------------------------
	// Implementation of ApplicationContext interface
	//---------------------------------------------------------------------
	/**
	 * Set a friendly name for this context.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Return a friendly name for this context.
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}
	
	/**
	 * Return the parent context, or {@code null} if there is no parent
	 * (that is, this context is the root of the context hierarchy).
	 */
	@Override
	public ApplicationContext getParent() {
		return this.parent;
	}
	
	@Override
	public void setParent(ApplicationContext parent) {
		this.parent = parent;
	}
	
	public long getStartupDate() {
		return startupDate;
	}
	
	//---------------------------------------------------------------------
	// Implementation of ConfigurableApplicationContext interface
	//---------------------------------------------------------------------

	@Override
	public void refresh() throws Exception {
		System.out.println(this.getClass().getName() + " ----------------- " + "refresh!");
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			/** 初始化BeanFactory工厂，生成所有Bean的BeanDefinition，并放入ioc容器中。*/
			// Tell the subclass to refresh the internal bean factory.
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
//			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
//				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
//				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
//				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
//				initMessageSource();

				// Initialize event multicaster for this context.
//				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
//				onRefresh();

				// Check for listener beans and register them.
//				registerListeners();

				/** 初始化单例Bean（非延迟初始化的那些Bean）*/
				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
//				finishRefresh();
			}

			catch (Throwable ex) {
				// Destroy already created singletons to avoid dangling resources.
//				destroyBeans();

				// Reset 'active' flag.
//				cancelRefresh(ex);

				// Propagate exception to caller.
				ex.printStackTrace();
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
//				resetCommonCaches();
			}
		}
	}

	private void prepareRefresh() {
		this.startupDate = System.currentTimeMillis();
	}

	/**
	 * 获取一个新的BeanFactory
	 */
	private BeanFactory obtainFreshBeanFactory() throws Exception {
		refreshBeanFactory(); // 创建新的BeanFactory
		return getBeanFactory();
	}
	
	/**
	 * Finish the initialization of this context's bean factory,
	 * initializing all remaining singleton beans.
	 */
	protected void finishBeanFactoryInitialization(DefaultListableBeanFactory beanFactory) {
		// Instantiate all remaining (non-lazy-init) singletons.
		beanFactory.preInstantiateSingletons();
	}
	
	//---------------------------------------------------------------------
	// Implementation of BeanFactory interface
	//---------------------------------------------------------------------

	@Override
	public Object getBean(String name) {
		return getBeanFactory().getBean(name);
	}
	
	
	//---------------------------------------------------------------------
	// Implementation of ListableBeanFactory interface
	//---------------------------------------------------------------------


	@Override
	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
		return getBeanFactory().getBeansWithAnnotation(annotationType);
	}
	
	
	
	//---------------------------------------------------------------------
	// Abstract methods that must be implemented by subclasses
	//---------------------------------------------------------------------

	/**
	 * Subclasses must implement this method to perform the actual configuration load.
	 * The method is invoked by {@link #refresh()} before any other initialization work.
	 * <p>A subclass will either create a new bean factory and hold a reference to it,
	 * or return a single BeanFactory instance that it holds. In the latter case, it will
	 * usually throw an IllegalStateException if refreshing the context more than once.
	 * @throws BeansException if initialization of the bean factory failed
	 * @throws IllegalStateException if already initialized and multiple refresh
	 * attempts are not supported
	 */
	protected abstract void refreshBeanFactory();

	/**
	 * Subclasses must implement this method to release their internal bean factory.
	 * This method gets invoked by {@link #close()} after all other shutdown work.
	 * <p>Should never throw an exception but rather log shutdown failures.
	 */
	protected abstract void closeBeanFactory();

	/**
	 * Subclasses must return their internal bean factory here. They should implement the
	 * lookup efficiently, so that it can be called repeatedly without a performance penalty.
	 * <p>Note: Subclasses should check whether the context is still active before
	 * returning the internal bean factory. The internal factory should generally be
	 * considered unavailable once the context has been closed.
	 * @return this application context's internal bean factory (never {@code null})
	 * @throws IllegalStateException if the context does not hold an internal bean factory yet
	 * (usually if {@link #refresh()} has never been called) or if the context has been
	 * closed already
	 * @see #refreshBeanFactory()
	 * @see #closeBeanFactory()
	 */
	@Override
	public abstract DefaultListableBeanFactory getBeanFactory() throws IllegalStateException;

}
