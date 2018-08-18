package clonegod.network.netty.rpc.registry;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 模拟服务注册中心的功能。
 *  
 */
public class ServiceRegistry {
	
	private static final String DEFAULT_SERVICE_PACKAGE = "clonegod.network.netty.rpc.provider";
	// 注册中心容器
	public static ConcurrentMap<String, Object> registryMap = new ConcurrentHashMap<>();
	
	
	public static void registry(String packageName) {
		if(packageName == null) {
			packageName = DEFAULT_SERVICE_PACKAGE;
		}
		doRegistry(packageName);
	}

	/**
	 * 扫描指定package下的class文件，通过反射将其实例化，注册到Map中
	 * 
	 */
	private static void doRegistry(String packageName) {
		try {
			String servicePackagePath = packageName.replaceAll("\\.", "/");
			URL url = ServiceRegistry.class.getClassLoader().getResource(servicePackagePath); // 获取classes目录下的文件资源
			
			Arrays.stream(Paths.get(url.toURI()).toFile().listFiles())
				// 过滤
				.filter(clazzFile -> clazzFile.getName().matches(".*Impl.class"))
				// 将class文件转换为带包名的类名
				.map(t -> (packageName + "." + t.getName().replace(".class", "")).trim())
				// 反射实例化，完成服务注册
				.forEach(className -> {
					try {
						Class<?> clazz = Class.forName(className);
						String interfaceName = clazz.getInterfaces()[0].getName();
						registryMap.put(interfaceName, clazz.newInstance());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			
			System.out.println("服务注册完成!");
			System.out.println(registryMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ServiceRegistry.registry(DEFAULT_SERVICE_PACKAGE);
	}
}
