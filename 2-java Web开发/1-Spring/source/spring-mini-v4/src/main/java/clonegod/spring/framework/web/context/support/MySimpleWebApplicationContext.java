package clonegod.spring.framework.web.context.support;

import clonegod.spring.framework.beans.factory.config.BeanDefinitionReader;
import clonegod.spring.framework.beans.factory.support.DefaultListableBeanFactory;

public class MySimpleWebApplicationContext extends AbstractRefreshableWebApplicationContext {
	
	/**
	 * 加载Bean配置，解析，将BeanDefinition存入IOC容器
	 */
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
		System.out.println(this.getClass().getSimpleName() + " start loadBeanDefinitions ...");
		
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		BeanDefinitionReader beanDefinitionReader = new BeanDefinitionReader(beanFactory);

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		loadBeanDefinitions(beanDefinitionReader);
	}

	private void loadBeanDefinitions(BeanDefinitionReader beanDefinitionReader) {
		String[] configLocations = super.getConfigLocations();
		if (configLocations != null) {
			for (String configLocation : configLocations) {
				beanDefinitionReader.loadBeanDefinitions(configLocation);
			}
		}
	}

	@Override
	protected String[] getDefaultConfigLocations() {
		return new String[] {"application-servlet.properties"};
	}

}
