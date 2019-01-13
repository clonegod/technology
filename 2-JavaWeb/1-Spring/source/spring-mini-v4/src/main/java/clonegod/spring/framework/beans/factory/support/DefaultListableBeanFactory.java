package clonegod.spring.framework.beans.factory.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clonegod.spring.framework.annotation.Autowired;
import clonegod.spring.framework.annotation.Controller;
import clonegod.spring.framework.annotation.Repository;
import clonegod.spring.framework.annotation.Service;
import clonegod.spring.framework.aop.AopConfig;
import clonegod.spring.framework.beans.BeanWrapper;
import clonegod.spring.framework.beans.BeanWrapperImpl;
import clonegod.spring.framework.beans.factory.BeanFactory;
import clonegod.spring.framework.beans.factory.BeanPostProcessor;
import clonegod.spring.framework.beans.factory.ListableBeanFactory;
import clonegod.spring.framework.beans.factory.ObjectFactory;
import clonegod.spring.framework.beans.factory.config.BeanDefinition;
import clonegod.spring.framework.web.context.WebApplicationUtil;


public class DefaultListableBeanFactory extends DefaultSingletonBeanRegistry implements ListableBeanFactory, BeanDefinitionRegistry  {
	
	/** Map of bean definition objects, keyed by bean name */
	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(256);
	
	/** List of bean definition names, in registration order */
	private volatile List<String> beanDefinitionNames = new ArrayList<String>(256);

	private BeanFactory parentBeanFactory;
	
	public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}

	/**
	 * 注册BeanDefinition
	 */
	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionNames.add(beanName);
		beanDefinitionMap.put(beanName, beanDefinition);
		for(Class<?> clazz : beanDefinition.getBeanClass().getInterfaces()) {
			beanDefinitionMap.put(clazz.getSimpleName(), beanDefinition);
		}
	}

	/**
	 * 
	 */
	public void preInstantiateSingletons() {
		System.out.println("preInstantiateSingletons。。。");
		// Iterate over a copy to allow for init methods which in turn register new bean definitions.
		// While this may not be part of the regular factory bootstrap, it does otherwise work fine.
		List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);
		
		// Trigger initialization of all non-lazy singleton beans...
		for (String beanName : beanNames) {
			BeanDefinition bd = getBeanDefinition(beanName);
			if(bd.isSingleton() && ! bd.isLazyInit()) {
				getBean(beanName);
			}
		}

		// Trigger post-initialization callback for all applicable beans...
//		for (String beanName : beanNames) {
//			Object singletonInstance = getSingleton(beanName);
//			smartSingleton.afterSingletonsInstantiated();
//		}
	}
	

	private BeanDefinition getBeanDefinition(String beanName) {
		return this.beanDefinitionMap.get(beanName);
	}

	
	@Override
	public Object getBean(String beanName) {
		Object bean;
		try {
			// Eagerly check singleton cache for manually registered singletons.
			Object sharedInstance = getSingletonProxy(beanName);
			if (sharedInstance != null) {
				return sharedInstance;
			}
			
			// Check if bean definition exists in this factory.
			BeanFactory parentBeanFactory = getParentFactory();
			if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
				return parentBeanFactory.getBean(beanName);
			}
			
			final BeanDefinition mbd = getBeanDefinition(beanName);
			if(mbd == null) {
				return null;
			}
			
			// Create bean instance.
			if (mbd.isSingleton()) {
				sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
					@Override
					public Object getObject() throws Exception {
						try {
							/** 实例化 */
							return createBean(beanName, mbd);
						}
						catch (Exception ex) {
							// Explicitly remove instance from singleton cache: It might have been put there
							// eagerly by the creation process, to allow for circular reference resolution.
							// Also remove any beans that received a temporary reference to the bean.
							destroySingleton(beanName);
							throw ex;
						}
					}
				});
			}

			else if (mbd.isPrototype()) {
				// It's a prototype -> create a new instance.
//				Object prototypeInstance = null;
//				try {
//					beforePrototypeCreation(beanName);
//					prototypeInstance = createBean(beanName, mbd, args);
//				}
//				finally {
//					afterPrototypeCreation(beanName);
//				}
//				bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
			}

			else {
				// 其它scope的处理
			}
			
			bean = sharedInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return bean; 
	}
	
	/**
	 * Central method of this class: 
	 * 	creates a bean instance,
	 * 	populates the bean instance, 
	 * 	applies post-processors, etc.
	 */
	private Object createBean(String beanName, BeanDefinition mbd) {
		Object exposedObject = null;
		BeanWrapperImpl instanceWrapper = null;
		try {
			// 1、 creates a bean instance
			Class<?> clazz = mbd.getBeanClass();
			Object beanInstance = clazz.newInstance();
			
			instanceWrapper = new BeanWrapperImpl(beanInstance);
			instanceWrapper.setAopConfig(initializeAopConfig(mbd)); // 配置AOP
			addSingletonProxy(beanName, instanceWrapper);
			
			final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null);
			
			// 2、 populates the bean instance
			/** populate - 填充  */
			// Initialize the bean instance.
			populateBean(beanName, mbd, instanceWrapper);
			
			// Initialize the bean instance.
			exposedObject = bean;
			
			if (exposedObject != null) {
				exposedObject = initializeBean(beanName, exposedObject, mbd);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
		// 返回给外部的是proxy对象，但ioc容器内部是原始对象
		return instanceWrapper.getWrappedInstance();
	}
	
	// 初始化AOP配置
	private AopConfig initializeAopConfig(BeanDefinition mbd) throws Exception {
		AopConfig config = new AopConfig();
		
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/application.properties"));
		String pointcut = props.getProperty("pointcut");
		String[] before = props.getProperty("aspectBefore").split("\\s+");
		String[] after = props.getProperty("aspectAfter").split("\\s+");
		
		Class<?> aspectClazz = Class.forName(before[0]);
		Object apspectInstance = aspectClazz.newInstance();
		
		// 判断Bean实例在的方法是否与pointcut匹配
		Pattern p = Pattern.compile(pointcut);
		for(Method method : mbd.getBeanClass().getDeclaredMethods()) {
			Matcher matcher = p.matcher(method.toString());
			if(matcher.matches()) {
				// 当method被调用时，将调用切面aspect对象的method[]
				config.addAspcet(method, 
						apspectInstance, 
						new Method[] {aspectClazz.getDeclaredMethod(before[1]), aspectClazz.getDeclaredMethod(after[1])});
			}
		}
		
		return config;
	}
	
	/**
	 * Initialize the given bean instance, applying factory callbacks as well as init methods and bean post processors.
	 */
	private Object initializeBean(String beanName, Object bean, BeanDefinition mbd) {
		
//		invokeAwareMethods(beanName, bean);
		
		BeanPostProcessor beanPostProcessor = new BeanPostProcessor() {};
		
		Object wrappedBean = bean;  
		wrappedBean = beanPostProcessor.postProcessBeforeInitialization(wrappedBean, beanName);

//		invokeInitMethods(beanName, wrappedBean, mbd);

		wrappedBean = beanPostProcessor.postProcessAfterInitialization(wrappedBean, beanName);
		
		return wrappedBean;
	}

	/**
	 * ------完成依赖注入
	 * 
	 * Populate the bean instance in the given BeanWrapper with the property values
	 * from the bean definition.
	 * @param beanName the name of the bean
	 * @param mbd the bean definition for the bean
	 * @param bw BeanWrapper with bean instance
	 */	
	protected void populateBean(String beanName, BeanDefinition mbd, BeanWrapper bw) {
		autowireByType(beanName, mbd, bw);
	}

	private void autowireByType(String beanName, BeanDefinition mbd, BeanWrapper bw) {
		try {
			Class<?> clazz = mbd.getBeanClass();
			if(clazz.isAnnotationPresent(Controller.class) ||
					clazz.isAnnotationPresent(Service.class) ||
					clazz.isAnnotationPresent(Repository.class)) {
				
				Field[] fields = clazz.getDeclaredFields();
				for(Field field : fields) {
					if(! field.isAnnotationPresent(Autowired.class)) {
						continue;
					}
					Autowired autowired = field.getAnnotation(Autowired.class);
					String autowiredBeanName = autowired.value();
					if(Objects.equals("", autowiredBeanName)) {
						autowiredBeanName = field.getType().getSimpleName();
					}
					Object autowiredInstance = null;
					if(getParentFactory() != null) {
						autowiredInstance = getParentFactory().getBean(autowiredBeanName);
					}
					if(autowiredInstance == null) {
						autowiredInstance = createBean(autowiredBeanName, this.beanDefinitionMap.get(autowiredBeanName));
					}
					field.setAccessible(true);
					if(getSingletonProxy(autowiredBeanName) != null) {
						autowiredInstance = ((BeanWrapper)getSingletonProxy(autowiredBeanName)).getWrappedInstance();
					}
					
					// Spring中依赖注入的都是代理对象
					Object proxyedInstance = null;
					if(Proxy.isProxyClass(autowiredInstance.getClass())) {
						proxyedInstance = autowiredInstance;
					} else {
						proxyedInstance = ((BeanWrapper) autowiredInstance).getWrappedInstance();
					}
					field.set(bw.getOriginalObject(), proxyedInstance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private BeanFactory getParentFactory() {
		return WebApplicationUtil.getParentFactory();
	}

	public void destroySingletons() {
		super.destroySingletons();
	}
	
	public boolean containsBeanDefinition(String beanName) {
		return this.beanDefinitionMap.containsKey(beanName);
	}

	@Override
	public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
		String[] beanNames = getBeanNamesForAnnotation(annotationType);
		Map<String, Object> results = new LinkedHashMap<String, Object>(beanNames.length);
		for (String beanName : beanNames) {
			results.put(beanName, this.getSingleton(beanName));
		}
		return results;
	}

	private String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
		List<String> results = new ArrayList<String>();
		for (String beanName : this.beanDefinitionNames) {
			BeanDefinition beanDefinition = getBeanDefinition(beanName);
			if (beanDefinition.getBeanClass().isAnnotationPresent(annotationType)) {
				results.add(beanName);
			}
		}
		return results.toArray(new String[results.size()]);
	}
	
}