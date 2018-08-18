package clonegod.spring.framework.beans.factory.support;

import clonegod.spring.framework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

	/**
	 * Register a new bean definition with this registry.
	 * 
	 * @param beanName
	 * @param beanDefinition
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
	
}
