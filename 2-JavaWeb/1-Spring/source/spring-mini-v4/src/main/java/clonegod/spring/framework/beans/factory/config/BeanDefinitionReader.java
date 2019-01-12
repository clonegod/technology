package clonegod.spring.framework.beans.factory.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import clonegod.spring.framework.beans.factory.support.BeanDefinitionReaderUtils;
import clonegod.spring.framework.beans.factory.support.BeanDefinitionRegistry;

/**
 * 从指定位置读取配置，解析得到BeanDefinition
 *
 */
public class BeanDefinitionReader {
	
	private final BeanDefinitionRegistry registry;

	public BeanDefinitionReader(BeanDefinitionRegistry beanFactory) {
		this.registry = beanFactory;
	}

	/**
	 * Return the bean factory to register the bean definitions with.
	 * <p>The factory is exposed through the BeanDefinitionRegistry interface,
	 * encapsulating the methods that are relevant for bean definition handling.
	 */
	BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}
	
	/**
	 * Load bean definitions from the specified resource location.
	 */
	public void loadBeanDefinitions(String location) {
		System.out.println("BeanDefinitionReader read: " + location);
		
		try {
			URL url = this.getClass().getResource("/"+location.replace("classpath:", ""));
			
			Properties properties = new Properties();
			properties.load(url.openStream());
			
			String[] packages = properties.getProperty("packageToScan").split(",");
			for(String packageName : packages) {
				if("".equals(packageName.trim())) {
					continue;
				}
				System.out.println("Scan package: " + packageName);
				URL packagePath = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"));
				File packageFolder = Paths.get(packagePath.toURI()).toFile();
				// 扫描给定包下的class文件
				for(File file : packageFolder.listFiles()) {
					String className = packageName + "." + file.getName().replace(".class", "");
					Class<?> clazz = Class.forName(className);
					doLoadBeanDefinitions(clazz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 加载Bean定义
	 * @param clazz
	 */
	private void doLoadBeanDefinitions(Class<?> clazz) {
		if(clazz.isInterface()) {
			return;
		}
		System.out.println("doLoadBeanDefinitions: " + clazz.getName());
		BeanDefinition beanDefinition = new BeanDefinition();
		// beanName的3种情况： 类名小写，自定义名称、接口类型注入
		String beanName = clazz.getSimpleName();
		beanDefinition.setBeanName(beanName);
		beanDefinition.setBeanClass(clazz);
		beanDefinition.setLazyInit(false);
		doRegisterBeanDefinitions(beanDefinition);
	}
	
	
	private void doRegisterBeanDefinitions(BeanDefinition beanDefinition) {
		BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinition, this.registry);
	}
	
	
}
