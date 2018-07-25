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

 

## IOC容器初始化流程详解
		
	以ClassPathXmlApplicationContext类初始化流程为例

注： ClassPathXmlApplicationContext 是 AbstractApplicationContext 的子类！


# 》》》 定位配置文件，refresh是关键入口
	// ---> ClassPathXmlApplicationContext

	public ClassPathXmlApplicationContext(
		String[] configLocations, boolean refresh, ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			// 将所有的Bean重新构造一遍
			refresh();
		}
	}

#
	// --->  AbstractApplicationContext

	@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			prepareRefresh();

			// 告诉子类刷新内部的BeanFactory
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


	/**
	 * Tell the subclass to refresh the internal bean factory.
	 * @return the fresh BeanFactory instance
	 * @see #refreshBeanFactory()
	 * @see #getBeanFactory()
	 */
	protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		refreshBeanFactory();
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		if (logger.isDebugEnabled()) {
			logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
		}
		return beanFactory;
	}

#
	// ---> AbstractRefreshableApplicationContext

	/**
	 * This implementation performs an actual refresh of this context's underlying
	 * bean factory, shutting down the previous bean factory (if any) and
	 * initializing a fresh bean factory for the next phase of the context's lifecycle.
	 */
	@Override
	protected final void refreshBeanFactory() throws BeansException {
		// 如果已经存在，则销毁
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			// 创建BeanFactory
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			beanFactory.setSerializationId(getId());
			customizeBeanFactory(beanFactory);
			// 加载资源，生成BeanDefinition，放入到beanFactory
			loadBeanDefinitions(beanFactory);
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
	
	protected DefaultListableBeanFactory createBeanFactory() {
		// 实例化BeanFactory，默认使用 DefaultListableBeanFactory
		return new DefaultListableBeanFactory(getInternalParentBeanFactory());
	}

	@Override
	protected final void closeBeanFactory() {
		// 同步销毁BeanFactory
		synchronized (this.beanFactoryMonitor) {
			this.beanFactory.setSerializationId(null);
			this.beanFactory = null;
		}
	}

	/**
	 * Determine whether this context currently holds a bean factory,
	 * i.e. has been refreshed at least once and not been closed yet.
	 */
	protected final boolean hasBeanFactory() {
		// 同步检查BeanFactory是否已经被创建
		synchronized (this.beanFactoryMonitor) {
			return (this.beanFactory != null);
		}
	}

	@Override
	public final ConfigurableListableBeanFactory getBeanFactory() {
		// 同步获取BeanFactory
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory == null) {
				throw new IllegalStateException("BeanFactory not initialized or already closed - " +
						"call 'refresh' before accessing beans via the ApplicationContext");
			}
			return this.beanFactory;
		}
	}


---
# 》》》 加载，解析，创建Beandefinition
#### 使用BeanDefinitionParserDelegate 解析得到 BeanDefinitionHolder
	
	// ---> AbstractXmlApplicationContext

	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// 创建XML类型的BeanDefinitionReader
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		initBeanDefinitionReader(beanDefinitionReader);
	
		// 开始执行loadBeanDefinitions
		loadBeanDefinitions(beanDefinitionReader);
	}

	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		// 获取所有spring配置文件的Resource资源
		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);
		}
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);
		}
	}

#
	// ---> AbstractBeanDefinitionReader
	@Override
	public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int counter = 0;
		// 循环所有的资源，进行加载，解析生成BeanDefinition
		for (String location : locations) {
			counter += loadBeanDefinitions(location);
		}
		return counter;
	}

	public int loadBeanDefinitions(String location, Set<Resource> actualResources) throws BeanDefinitionStoreException {
		ResourceLoader resourceLoader = getResourceLoader();
		// ...省略

		if (resourceLoader instanceof ResourcePatternResolver) {
			// Resource pattern matching available.
			try {
				Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
				int loadCount = loadBeanDefinitions(resources);
				// ...省略
				return loadCount;
			}
			catch (IOException ex) {
			}
		}
		else {
			// Can only load single resources by absolute URL.
			Resource resource = resourceLoader.getResource(location);
			int loadCount = loadBeanDefinitions(resource);
			// ...省略
			return loadCount;
		}
	}

	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		Assert.notNull(resources, "Resource array must not be null");
		int counter = 0;
		// 循环调用，加载每个Resource
		for (Resource resource : resources) {
			// loadBeanDefinitions有三种不同的实现：
			//		AnnotationConfigWebApplicationContext
			//		XmlWebApplicationContext
			//		GroovyWebApplicationContext
			counter += loadBeanDefinitions(resource);
		}
		return counter;
	}

#
	// ---> XmlBeanDefinitionReader
	
	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}

	/**
	 * Load bean definitions from the specified XML file.
	 * @param encodedResource the resource descriptor for the XML file,
	 * allowing to specify an encoding to use for parsing the file
	 * @return the number of bean definitions found
	 * @throws BeanDefinitionStoreException in case of loading or parsing errors
	 */
	public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {

		// ...省略

		try {
			// 获取资源
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				// doLoadBeanDefinitions是真正执行加载的方法
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}

	protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {
		try {
			// 读取资源，转换为Document对象
			Document doc = doLoadDocument(inputSource, resource);
			// 注册BeanDefinition
			return registerBeanDefinitions(doc, resource);
		}
		catch (BeanDefinitionStoreException ex) {
			throw ex;
		}
		catch (SAXParseException ex) {
			throw new XmlBeanDefinitionStoreException(resource.getDescription(),
					"Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
		}
		catch (SAXException ex) {
			throw new XmlBeanDefinitionStoreException(resource.getDescription(),
					"XML document from " + resource + " is invalid", ex);
		}
		catch (ParserConfigurationException ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"Parser configuration exception parsing XML from " + resource, ex);
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"IOException parsing XML document from " + resource, ex);
		}
		catch (Throwable ex) {
			throw new BeanDefinitionStoreException(resource.getDescription(),
					"Unexpected exception parsing XML document from " + resource, ex);
		}
	}
	
	// ---> DefaultBeanDefinitionDocumentReader 

	/**
	 * Register the bean definitions contained in the given DOM document.
	 */
	public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		int countBefore = getRegistry().getBeanDefinitionCount();
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}

	protected void doRegisterBeanDefinitions(Element root) {
		// Any nested <beans> elements will cause recursion in this method. In
		// order to propagate and preserve <beans> default-* attributes correctly,
		// keep track of the current (parent) delegate, which may be null. Create
		// the new (child) delegate with a reference to the parent for fallback purposes,
		// then ultimately reset this.delegate back to its original (parent) reference.
		// this behavior emulates a stack of delegates without actually necessitating one.
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);

		if (this.delegate.isDefaultNamespace(root)) {
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isInfoEnabled()) {
						logger.info("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}

		preProcessXml(root); 

		// 委派给BeanDefinitionParserDelegate进行解析
		parseBeanDefinitions(root, this.delegate);

		postProcessXml(root);

		this.delegate = parent;
	}
		


	/**
	 * Process the given bean element, parsing the bean definition
	 * and registering it with the registry.
	 */
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		// Holder 是 BeanDefinition的装饰器，内部包含BeanDefinition、beanName、aliases别名信息
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
				// 通过BeanDefinitionReaderUtils工具类，进行BeanDefinition的注册
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}

---
# 》》》 注册Beandefinition

### 在 BeanDefinitionReaderUtils 中，根据 BeanDefinitionHolder 完成Definition的注册

### 底层使用ConcurrentHashMap作为beanDefinition的容器

#
	// --->  BeanDefinitionReaderUtils

	/**
	 * Register the given bean definition with the given bean factory.
	 * @param definitionHolder the bean definition including name and aliases
	 * @param registry the bean factory to register with
	 * @throws BeanDefinitionStoreException if registration failed
	 */
	public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
			throws BeanDefinitionStoreException {

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
		// BeanDefinitionRegistry有三种实现：
		//	 DefaultListableBeanFactory	
		//   GenericApplicationContext
		//	 SimpleBeanDefinitionRegistry
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

		// 如果有别名，则绑定别名与beanName
		// Register aliases for bean name, if any.
		String[] aliases = definitionHolder.getAliases();
		if (aliases != null) {
			for (String alias : aliases) {
				registry.registerAlias(beanName, alias);
			}
		}
	}

	
#
	// ---> SimpleBeanDefinitionRegistry
	
	// ConcurrentHashMap存储BeanDefinition
	/** Map of bean definition objects, keyed by bean name */
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);


	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

		Assert.hasText(beanName, "'beanName' must not be empty");
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");
		this.beanDefinitionMap.put(beanName, beanDefinition);
	}
	

#
	// ---> GenericApplicationContext
	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {
		// 通过beanFactory进行BeanDefinition的注册
		// 而beanFactory实际上是DefaultListableBeanFactory，所以实际是在DefaultListableBeanFactory中进行的注册
		this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
	}


#
	// ---> DefaultListableBeanFactory
	
	// ConcurrentHashMap存储BeanDefinition
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {
		// ...省略

		BeanDefinition oldBeanDefinition;

		oldBeanDefinition = this.beanDefinitionMap.get(beanName);
		if (oldBeanDefinition != null) {
			// ... 省略
			this.beanDefinitionMap.put(beanName, beanDefinition);
		}
		else {
			if (hasBeanCreationStarted()) {
				// Cannot modify startup-time collection elements anymore (for stable iteration)
				synchronized (this.beanDefinitionMap) {
					this.beanDefinitionMap.put(beanName, beanDefinition);
					// ...省略
				}
			}
			else {
				// Still in startup registration phase
				this.beanDefinitionMap.put(beanName, beanDefinition);
				this.beanDefinitionNames.add(beanName);
				this.manualSingletonNames.remove(beanName);
			}
			this.frozenBeanDefinitionNames = null;
		}

		if (oldBeanDefinition != null || containsSingleton(beanName)) {
			resetBeanDefinition(beanName);
		}
	}