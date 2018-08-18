## IOC 与 DI 的区别？
	IOC 指的是 Inverse Of Control ，强调控制反转
		既然要控制反转，那么对象的创建就不再是显示使用new关键字创建，而是由容器来创建并管理
		IOC容器：Map容器存储所有实例化的Bean实例

	DI 指的是 Dependency Inject， 强调依赖注入：
		依赖注入：根据代码中所声明需要的依赖，由spring自动将容器中满足条件的对象注入到代码中
		依赖注入的方式： setter, constructor (反射技术)


	
## BeanFactory
	BeanFactory
		|-AutowireCapableBeanFactory 
		|-HierarchicalBeanFactory	处理具有层次关系的场景，比如类之间具有继承关系 
		|-ListableBeanFactory 处理集合类型的场景

			|-DefaultListableBeanFactory 它是BeanFactory底层的默认实现类
	
## IOC容器的初始化流程？	
	IOC容器的初始化包括BeanDefinition的Resource定位、载入和注册这三个过程。
	以ApplicationContext的子类为例：
		ClassPathXmlApplicationContext

	定位 -> 加载 -> 注册
		
	【定位】
	定位，即定位资源的位置：
		类路径下的配置文件，比如applicationContext.xml
		@ComponentScan 所指定的package下标记了@Component之类注解的类

	BeanDefinitionReader
		从XML读取Bean的配置，存储到BeanDefinition中。
		实现类：比如 XmlBeanDefinitionReader，从XML加载Bean定义。
	
	【加载】
	加载，即读取资源文件的内容，将其转换为BeanDefinition。
	BeanDefinition
		封装了最原始Bean定义的相关数据。
		最主要的目的是：封装Bean的元数据，之后根据该定义来创建和扩展Bean的功能。
				
				
	【注册】
	注册，即将BeanDefinition的内容写入到Map中。
	将用户所定义的Bean放入IOC容器中（底层使用Map进行存储）,完成注册的过程。
	注意：往IOC容器中注册的是BeanDefinition，不是真正的Bean实例。
		对于单例Bean而言，其实例是放在一个cacheMap容器中缓存起来的。
 

## Bean的依赖注入
	spring中的Bean默认是单例的
	spring中的Bean默认是init-lazy的
	
	单例Bean的底层实际是通过一个cachedMap来存储Bean，之后直接从这个Map获取。

	多例Bean，则是每次获取的时候，从新创建一个新的对象。

	》》》 依赖注入的时机：
		Bean之间有依赖关系，所以在实例化一个Bean的时候，如果依赖其他的Bean，就需要进行注入。
		因此，注入的时机是在Bean实例化的时候发生的。


## BeanWrapper
	Wrapper对原生对象进行了包装，放入底层cache的是Bean的Wrapper对象。
	使用Wrapper对Bean进行包装的目的：
	  不侵入原始的Bean对象，在原有对象的基础上，再进行扩展，比如实现回调函数、监听器等功能

## 单例Bean的初始化过程？
spring在启动时，

经过定位配置文件、加载配置、注册BeanDefinition三个步骤，将BeanDefinition缓存到IOC容器中。

接着，在refresh()方法中，获取BeanFactory，

然后调用beanFactory.preInstantiateSingletons()来初始化所有非lazy-init的Bean实例

	// ---> AbstractApplicaitonContext

	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();
			
			// 根据子类的不同实现，获取对应的BeanFactory
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
			
				// 初始化所有单例Bean(没有配置lazy-init的Bean)
				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

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


	protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {

		// Allow for caching all bean definition metadata, not expecting further changes.
		beanFactory.freezeConfiguration();
		
		// 预先实例化单例Bean
		// Instantiate all remaining (non-lazy-init) singletons.
		beanFactory.preInstantiateSingletons();
	}

#	
	// ---> DefaultListableBeanFactory
	@Override
	public void preInstantiateSingletons() throws BeansException {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Pre-instantiating singletons in " + this);
		}

		// Iterate over a copy to allow for init methods which in turn register new bean definitions.
		// While this may not be part of the regular factory bootstrap, it does otherwise work fine.
		List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);
		
		// 循环所有beanNames，初始化所有的Singleton Bean 实例
		// Trigger initialization of all non-lazy singleton beans...
		for (String beanName : beanNames) {
			RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
			if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
				if (isFactoryBean(beanName)) {
					final FactoryBean<?> factory = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
					boolean isEagerInit;
					if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
						isEagerInit = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
							@Override
							public Boolean run() {
								return ((SmartFactoryBean<?>) factory).isEagerInit();
							}
						}, getAccessControlContext());
					}
					else {
						isEagerInit = (factory instanceof SmartFactoryBean &&
								((SmartFactoryBean<?>) factory).isEagerInit());
					}
					if (isEagerInit) {
						// Bean的实例化通过getBean()完成
						getBean(beanName);
					}
				}
				else {
					getBean(beanName);
				}
			}
		}

		// Trigger post-initialization callback for all applicable beans...
		for (String beanName : beanNames) {
			Object singletonInstance = getSingleton(beanName);
			if (singletonInstance instanceof SmartInitializingSingleton) {
				final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged(new PrivilegedAction<Object>() {
						@Override
						public Object run() {
							smartSingleton.afterSingletonsInstantiated();
							return null;
						}
					}, getAccessControlContext());
				}
				else {
					smartSingleton.afterSingletonsInstantiated();
				}
			}
		}
	}
