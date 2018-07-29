package clonegod.spring.mini.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import clonegod.spring.mini.annotation.Autowired;
import clonegod.spring.mini.annotation.Controller;
import clonegod.spring.mini.annotation.Service;
import clonegod.spring.mini.controller.SimpleController;

/**
 * 模拟spring IOC容器的初始化过程
 *
 */
public class IOCContainer {
	
	private Properties props = new Properties();
	private Set<String> clsNames = new HashSet<>();
	private ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();
	
	/**
	 * 模拟spring IOC容器的初始化流程：
	 * 	定位、加载、注册，以及依赖注入
	 */
	public void init(String contextConfigLocation) {
		try {
			doLoadConfig(contextConfigLocation); 	// 定位、加载
			doScan();
			doRegistry(); 		// 注册
			doAutowired(); 		// 依赖注入
			doHandlerMapping(); 	// 绑定路由映射
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void doLoadConfig(String contextConfigLocation) {
		System.out.println("1、加载配置文件");
		try (InputStream inStream = IOCContainer.class.getResourceAsStream("/application.properties");) {
			props.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doScan() {
		System.out.println("2、解析配置");
		String packageToScan = props.getProperty("packageToScan");
		String[] pacakges = packageToScan.split(",");
		for(final String pkg : pacakges ) {
			try {
				URI packageURI = getClass().getClassLoader().getResource(pkg.replaceAll("\\.", "/")).toURI();
				Files.list(Paths.get(packageURI)).forEach(clsPath -> {
					clsNames.add(pkg + "." + clsPath.toFile().getName().replaceAll(".class", ""));
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		System.out.println(clsNames);
	}

	private void doRegistry() {
		System.out.println("3、注册所有的bean到IOC容器");
		clsNames.forEach(clsName -> {
			try {
				Class<?> clazz = Class.forName(clsName);
				
				if(clazz.isAnnotationPresent(Controller.class)) {
					String beanName = classNameToLowerCase(clazz.getSimpleName());
					beanMap.put(beanName, clazz.newInstance());
					
				} else if (clazz.isAnnotationPresent(Service.class)) {
					// 优先自定义的bean name
					Service service = clazz.getAnnotation(Service.class);
					String beanName = service.value();
					if("".equals(beanName)) {
						// 如果没有自定义bean name, 则默认为类名
						beanName = classNameToLowerCase(clazz.getSimpleName());
					}
					
					Object instance = clazz.newInstance();
					beanMap.put(beanName, instance);
					
					// 依赖注入的时候可能是按接口类型注入的，因此要将接口与实例进行绑定
					for(Class<?> interfaceCls : clazz.getInterfaces()) {
						beanName = classNameToLowerCase(interfaceCls.getSimpleName());
						beanMap.put(beanName, instance);
					}
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println(beanMap);
	}

	private void doAutowired() {
		System.out.println("4、执行依赖注入");
		beanMap.forEach((key, bean) -> {
			Field[] fields = bean.getClass().getDeclaredFields();
			for(Field field : fields) {
				if(field.isAnnotationPresent(Autowired.class)) {
					// 自定义beanName优先
					Autowired autowired = field.getAnnotation(Autowired.class);
					String beanName = autowired.value();
					// 如果没有自定义beanName，则按该字段的类型处理
					if("".equals(beanName.trim())) {
						beanName =  field.getType().getSimpleName();
					}
					Object otherBean = beanMap.get(classNameToLowerCase(beanName));
					field.setAccessible(true);
					try {
						field.set(bean, otherBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void doHandlerMapping() {
		System.out.println("5、绑定路由映射，将http请求的uri与controller中的handler进行绑定");
		SimpleController controlelr = (SimpleController) beanMap.get("simpleController");
		String result = controlelr.sayHello("Alice");
		System.out.println(result);
	}
	
	private String classNameToLowerCase(String name) {
		char[] chars = name.toCharArray();
		chars[0] += 32;
		return new String(chars);
	}
}
