package clonegod.spring.framework.beans.factory.support;

import clonegod.spring.framework.beans.factory.config.BeanDefinition;

public class BeanDefinitionReaderUtils {

	/**
	 * Register the given bean definition with the given bean factory.
	 * 
	 * @param beanDefinition
	 * @param registry
	 */
	public static void registerBeanDefinition(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
		// Register bean definition under primary name.
		String beanName = beanDefinition.getBeanName();
		registry.registerBeanDefinition(beanName, beanDefinition);
	}
	
}
