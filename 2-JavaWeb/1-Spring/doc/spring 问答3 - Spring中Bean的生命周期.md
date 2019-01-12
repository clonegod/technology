# Spring中Bean的生命周期？
	》》》初始化阶段
		IOC容器初始化的时候，会根据用户配置实例化相关的Bean实例，并存储到IOC容器中。

	》》》使用阶段
		依赖注入到相关业务Bean中进行使用

	》》》销毁阶段
		销毁，即从底层的BeanMap中remove相关的bean。
		1、正常销毁
			容器接收到停止操作，比如ctrl+c
			
		2、异常销毁
			初始化IOC容器的时候，如果发生异常，则会销毁已经初始化的Bean。
			可能的原因：
				加载配置文件出错、解析配置出错、初始化时抛出的不可预知异常等

##### Bean正常销毁过程
ApplicationContext对stop()的是通过事件发布机制来实现的：

	// 1、Servlet容器关闭时，自动回调的方法，目的是进行资源回收
	public abstract class GenericServlet implements Servlet, ServletConfig,
    java.io.Serializable {
	    /**
	     * Called by the servlet container to indicate to a servlet that the servlet
	     * is being taken out of service. See {@link Servlet#destroy}.
	     */
	    @Override
	    public void destroy() {
	        // NOOP by default
	    }
		// ... 
	}
	
	// 2、在Spring的FrameworkServlet 中实现了destroy()的逻辑
	public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
		/**
		 * Close the WebApplicationContext of this servlet.
		 */
		@Override
		public void destroy() {
			// Only call close() on WebApplicationContext if locally managed...
			if (this.webApplicationContext instanceof ConfigurableApplicationContext && !this.webApplicationContextInjected) {
			// 调用close()，执行IOC容器关闭操作
				((ConfigurableApplicationContext) this.webApplicationContext).close();
			}
		}
	}

	// 3、真正关闭容器的逻辑在 AbstractApplicationContext 中
	public void close() {
		synchronized (this.startupShutdownMonitor) {
			// 执行关闭操作
			doClose();
			// If we registered a JVM shutdown hook, we don't need it anymore now:
			// We've already explicitly closed the context.
			if (this.shutdownHook != null) {
				try {
					Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
				}
				catch (IllegalStateException ex) {
					// ignore - VM is already shutting down
				}
			}
		}
	}
	
	// 关闭容器，销毁已经实例化的Bean
	protected void doClose() {
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			
			try {
				// 发布ContextClosedEvent，由ContextCloserListener接收并处理该事件
				// Publish shutdown event.  
				publishEvent(new ContextClosedEvent(this));
			}
			catch (Throwable ex) {
				logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
			}
			// ...

			// Destroy all cached singletons in the context's BeanFactory.
			destroyBeans();

			// Close the state of this context itself.
			closeBeanFactory();

			// Let subclasses do some final clean-up if they wish...
			onClose();

			this.active.set(false);
		}
	}

	protected void publishEvent(Object event, ResolvableType eventType) {
		// ... 省略

		// Multicast right now if possible - or lazily once the multicaster is initialized
		if (this.earlyApplicationEvents != null) {
			this.earlyApplicationEvents.add(applicationEvent);
		}
		else {
			// 广播事件
			getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
		}
		
		// 发布close事件
		// Publish event via parent context as well...
		if (this.parent != null) {
			if (this.parent instanceof AbstractApplicationContext) {
				((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
			}
			else {
				this.parent.publishEvent(event);
			}
		}
	}
	

	// ContextCloserListener 监听容器关闭事件
	protected static class ContextCloserListener
		implements ApplicationListener<ContextClosedEvent> {

		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			ConfigurableApplicationContext context = this.childContext.get();
			if ((context != null)
					&& (event.getApplicationContext() == context.getParent())
					&& context.isActive()) {
				 // 执行关闭，实际执行AbstractApplicationContext中的close()
				context.close();
			}
		}
		// ...
	}


##### Bean异常销毁过程
	
	// AbstractApplicationContext 
	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}
			catch (BeansException ex) {
				// 初始IOC容器的过程中，发生异常，则执行销毁动作，并抛出异常，容器初始化失败，程序结束。
				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}

	protected void destroyBeans() {
		getBeanFactory().destroySingletons();
	}


	// DefaultListableBeanFactory
	@Override
	public void destroySingletons() {
		super.destroySingletons();
		this.manualSingletonNames.clear();
		clearByTypeCache();
	}

	
	// ---> DefaultSingletonBeanRegistry
	// 销毁所有已经实例化的单例Bean
	public void destroySingletons() {
		String[] disposableBeanNames;
		synchronized (this.disposableBeans) {
			disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
		}
		// 从底层Map中删除Bean，并调用bean的destroy()
		for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
			destroySingleton(disposableBeanNames[i]);
		}
		
		// clear maps
		this.containedBeanMap.clear();
		this.dependentBeanMap.clear();
		this.dependenciesForBeanMap.clear();

		synchronized (this.singletonObjects) {
			this.singletonObjects.clear();
			this.singletonFactories.clear();
			this.earlySingletonObjects.clear();
			this.registeredSingletons.clear();
			this.singletonsCurrentlyInDestruction = false;
		}
	}