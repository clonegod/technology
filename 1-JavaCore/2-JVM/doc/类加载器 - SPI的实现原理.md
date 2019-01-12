## SPI（以JDBC为例）机制 VS 类加载器

### 什么是SPI？
	Java SPI 实际上是“基于接口的编程＋策略模式＋配置文件”组合实现的动态加载机制。
	
	使用 ServiceLoader 来加载配置文件中指定的实现

	SPI 的应用之一是可替换的插件机制，是对服务接口的统一抽象。

SPI 全称为 (Service Provider Interface) ,是JDK内置的一种服务提供发现机制。 目前有不少框架用它来做服务的扩展发现， 简单来说，它就是一种动态替换发现的机制， 举个例子来说， 有个接口，想运行时动态的给它添加实现，你只需要添加一个实现。

具体是在JAR包的"src/META-INF/services/"目录下建立一个文件，文件名是接口的全限定名，文件的内容可以有多行，每行都是该接口对应的具体实现类的全限定名.

比如 JDBC 数据库驱动包，mysql-connector-java-5.1.18.jar 就有一个 /META-INF/services/java.sql.Driver 里面内容是 com.mysql.jdbc.Driver 。


#### what's java spi ?
是上游产商给服务供应商提供的接口，供应商遵循接口契约提供自己的实现。

提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。 

基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。简单来讲就是为某个接口寻找服务实现的机制。

### SPI 与 类加载器有什么关系？

类加载机制按双亲委派模型执行，要实现SPI功能，则带来了麻烦。

因为实现SPI，需要上层的ClassLoader去加载下层的类，而“双亲委派”模式下，只能下层访问上层加载的类，因此必须打破双亲委派模型的限制。

》》》 解决办法：
	
通过Thread.setContextLoader将下层的AppClassLoader进行保存，当在BootstrapClassLoader所加载的类中，需要用到底层类加载器所加载的类时，再从Thread.getContextLoader获取到底层类加载器(AppClassLoader)，从而间接地利用AppClassLoader得到需要的相关的class。


### JDBC 为什么要设计为 SPI， 有什么好处？
[看这里](https://stackoverflow.com/questions/11376508/service-provider-interface-without-the-provider)  


---

### SPI 的实践案例 - 从JDBC的设计看SPI是怎么用的

	Java设计者将数据库访问抽象为SPI接口形式，不同数据库厂商按JDBC规范提供对应的驱动包。
	在运行时，由ServiceLoader加载驱动包，自动创建好Connection对象供开发者使用。


##### 首先，看一下Java程序中如何获取JBDC连接的

	package jvm.classloader;
	
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.SQLException;

	public class SPIJDBCClassLoaderDemo {
		
		public static void main(String[] args) {
			try {
				// 1、告诉类加载器加载 com.mysql.jdbc.Driver
				Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
				// sun.misc.Launcher$AppClassLoader@2a139a55
				// 说明Connection是被AppClassLoader加载的
				System.out.println(clazz.getClassLoader());
				
				// 2、从DriverManager获取Connecton
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "alice", "alice123");
				// com.mysql.jdbc.JDBC4Connection@5fa7e7ff
				System.out.println(conn);
				
				// return null if this class was loaded by the bootstrap class loader. 
				// 3、返回null -> 说明 DriverManager是被Bootstrap classLoader 加载的
				System.out.println(DriverManager.class.getClassLoader()); 
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	
##### com.mysql.jdbc.Driver 将自己注册到 DriverManager 中

	public class Driver extends NonRegisteringDriver implements java.sql.Driver {
	    // Register ourselves with the DriverManager
	    static {
	        try {
	            java.sql.DriverManager.registerDriver(new Driver());
	        } catch (SQLException E) {
	            throw new RuntimeException("Can't register driver!");
	        }
	    }

	}
 
##### 接下来，看DriverManager中是怎样创建Connection的？
	
	public class DriverManager {
		// DriverManager 内部维护一个CopyOnWriteArrayList集合，存放各种数据库Driver
		// List of registered JDBC drivers
    	private final static CopyOnWriteArrayList<DriverInfo> registeredDrivers = new CopyOnWriteArrayList<>();
		
		// Driver会在类初始化阶段将自己注册到DriverManager中
		public static synchronized void registerDriver(java.sql.Driver driver,
            DriverAction da)
        throws SQLException {
	        /* Register the driver if it has not already been added to our list */
	        if(driver != null) {
				// 注册
	            registeredDrivers.addIfAbsent(new DriverInfo(driver, da));
	        } else {
	            // This is for compatibility with the original DriverManager
	            throw new NullPointerException();
	        }
	        println("registerDriver: " + driver);
	    }
		
	    /**
	     * Load the initial JDBC drivers by checking the System property
	     * jdbc.properties and then use the {@code ServiceLoader} mechanism
	     */
	    static {
			// 初始化Driver
	        loadInitialDrivers();
	        println("JDBC DriverManager initialized");
	    }

		private static void loadInitialDrivers() {
	        String drivers;

	        // If the driver is packaged as a Service Provider, load it.
	        // Get all the drivers through the classloader
	        // exposed as a java.sql.Driver.class service.
	        // ServiceLoader.load() replaces the sun.misc.Providers()
	
	        AccessController.doPrivileged(new PrivilegedAction<Void>() {
	            public Void run() {
					// 通过ServiceLoader间接得到AppClassLoader加载的类：com.mysql.jdbc.Driver
	                ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
	                Iterator<Driver> driversIterator = loadedDrivers.iterator();
	
	                /* Load these drivers, so that they can be instantiated.
	                 * It may be the case that the driver class may not be there
	                 * i.e. there may be a packaged driver with the service class
	                 * as implementation of java.sql.Driver but the actual class
	                 * may be missing. In that case a java.util.ServiceConfigurationError
	                 * will be thrown at runtime by the VM trying to locate
	                 * and load the service.
	                 *
	                 * Adding a try catch block to catch those runtime errors
	                 * if driver not available in classpath but it's
	                 * packaged as service and that service is there in classpath.
	                 */
	                try{
	                    while(driversIterator.hasNext()) {
	                        driversIterator.next();
	                    }
	                } catch(Throwable t) {
	                // Do nothing
	                }
	                return null;
	            }
	        });
	
	        println("DriverManager.initialize: jdbc.drivers = " + drivers);

	        String[] driversList = drivers.split(":");
	        println("number of Drivers:" + driversList.length);
	        for (String aDriver : driversList) {
	            try {
	                println("DriverManager.Initialize: loading " + aDriver);
	                Class.forName(aDriver, true,
	                        ClassLoader.getSystemClassLoader());
	            } catch (Exception ex) {
	                println("DriverManager.Initialize: load failed: " + ex);
	            }
	        }
	    }
		
		private static Connection getConnection(
        String url, java.util.Properties info, Class<?> caller) throws SQLException {
	        /*
	         * When callerCl is null, we should check the application's
	         * (which is invoking this class indirectly)
	         * classloader, so that the JDBC driver class outside rt.jar
	         * can be loaded from here.
	         */
	        ClassLoader callerCL = caller != null ? caller.getClassLoader() : null;
	        synchronized(DriverManager.class) {
	            // synchronize loading of the correct classloader.
	            if (callerCL == null) {
					// 从ContextClassLoader中得到就是 AppClassLoader
	                callerCL = Thread.currentThread().getContextClassLoader();
	            }
	        }
	
	        println("DriverManager.getConnection(\"" + url + "\")");
	
	        // Walk through the loaded registeredDrivers attempting to make a connection.
	        // Remember the first exception that gets raised so we can reraise it.
	        SQLException reason = null;
	
	        for(DriverInfo aDriver : registeredDrivers) {
	            // If the caller does not have permission to load the driver then
	            // skip it.
	            if(isDriverAllowed(aDriver.driver, callerCL)) {
	                try {
	                    println("    trying " + aDriver.driver.getClass().getName()); 
						// 创建 Connection
	                    Connection con = aDriver.driver.connect(url, info);
	                    if (con != null) {
	                        // Success!
	                        println("getConnection returning " + aDriver.driver.getClass().getName());
							// 返回 Connection
	                        return (con);
	                    }
	                } catch (SQLException ex) {
	                    if (reason == null) {
	                        reason = ex;
	                    }
	                }
	
	            } else {
	                println("    skipping: " + aDriver.getClass().getName());
	            }
	
	        }
	
	        // if we got here nobody could connect.
	        if (reason != null)    {
	            println("getConnection failed: " + reason);
	            throw reason;
	        }
	
	        println("getConnection: no suitable driver found for "+ url);
	        throw new SQLException("No suitable driver found for "+ url, "08001");
	    }


	}
	
#
	public final class ServiceLoader<S> implements Iterable<S> {
		public static <S> ServiceLoader<S> load(Class<S> service) {
			// 使用ContextClassLoader类加载器，来加载指定的service
			// 实际上，这里的ContextClassLoader是AppClassLoader
			// 解决Bootstrap ClassLoader无法加载到AppClassLoader所加载的类 - Driver.class
	        ClassLoader cl = Thread.currentThread().getContextClassLoader();
	        return ServiceLoader.load(service, cl);
	    }
	}


#
	public abstract class ClassLoader {

		@CallerSensitive
	    public static ClassLoader getSystemClassLoader() {
	        initSystemClassLoader(); // 初始化SystemClassLoader
	        if (scl == null) {
	            return null;
	        }
	        SecurityManager sm = System.getSecurityManager();
	        if (sm != null) {
	            checkClassLoaderPermission(scl, Reflection.getCallerClass());
	        }
	        return scl;
	    }

		private static synchronized void initSystemClassLoader() {
	        if (!sclSet) {
	            if (scl != null)
	                throw new IllegalStateException("recursive invocation");
	            sun.misc.Launcher l = sun.misc.Launcher.getLauncher();
	            if (l != null) {
	                Throwable oops = null;
	                scl = l.getClassLoader();
	                try {
						// doPrivileged 是一个Native方法
						// 实例化SystemClassLoaderAction
						// AccessController.doPrivileged在enabling privileges之后，就会调用SystemClassLoaderAction的run()，返回SystemClassLoader，并设置为ContextClassLoader
	                    scl = AccessController.doPrivileged(new SystemClassLoaderAction(scl));
	                } catch (PrivilegedActionException pae) {
	                    oops = pae.getCause();
	                    if (oops instanceof InvocationTargetException) {
	                        oops = oops.getCause();
	                    }
	                }
	            }
	            sclSet = true;
	        }
	    }
		
		class SystemClassLoaderAction  implements PrivilegedExceptionAction<ClassLoader> {
		    private ClassLoader parent;
		
		    SystemClassLoaderAction(ClassLoader parent) {
		        this.parent = parent;
		    }
			
			// This method will be called by AccessController.doPrivileged after enabling privileges.
		    public ClassLoader run() throws Exception {
		        String cls = System.getProperty("java.system.class.loader");
		        if (cls == null) {
		            return parent;
		        }
		
		        Constructor<?> ctor = Class.forName(cls, true, parent)
		            .getDeclaredConstructor(new Class<?>[] { ClassLoader.class });
		        ClassLoader sys = (ClassLoader) ctor.newInstance(
		            new Object[] { parent });

				// 设置ContextClassLoader为SystemClassLoader,即AppClassLoader
				// 解决双亲模式下，上层无法访问下层加载到的类的问题
		        Thread.currentThread().setContextClassLoader(sys);
		        return sys;
		    }
		}	

	}

