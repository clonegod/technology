# Lesson 20 | 类加载

## 类加载流程


## 类加载器


---
## 什么是 jar hell？ 如何解决？

jar hell 指的是相同类名的class文件，出现在不同的jar包中。

	解决办法：
	1、通过依赖管理工具，比如Maven，手动排除掉冲突的jar；
	2、通过程序获取冲突的class文件所在的jar
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String resourceName = "net/sf/cglib/proxy/MethodInterceptor.class";
			Enumeration<URL> urls = classLoader.getResources(resourceName);
			while(urls.hasMoreElements()){
				System.out.println(urls.nextElement());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	3、自定义类加载，指定加载正确的jar包。


## Tomcat 的类加载为什么要打破双亲委派？
	Everything about Apache Tomcat aims to be as self-contained, intuitive, and automatic as possible, in an effort to standardize the configuration and deployment of web applications for efficient administration, while limiting access to different libraries for security and namespace reasons. 

	This is why rather than using the Java "classpath" environment variable, which is the traditional place to declare dependency repositories, Tomcat's start scripts ignore this variable and generate their own classpaths when they create Tomcat's "system" classloader.
	
	In other words, if you've been going mad trying to declare additional repositories in your system's environment variables, the reason you've been frustrated over and over is that Tomcat has been writing over your work every time it boots.
	
	To understand how Tomcat resolves classpath, take a look at this outline of the Tomcat 6 startup process:
	
	The JVM bootstrap loader loads the core Java libraries. Incidentally, this is the one place where environment variables do matter, as the JVM locates the core libraries using the JAVA_HOME variable.
	Startup.sh, calling Catalina.sh with the "start" parameter, overwrites the system classpath and loads bootstrap.jar and tomcat-juli.jar. These resources are only visible to Tomcat.
	Class loaders are created for each deployed Context, which load all classes and JAR files contained in each web application's WEB-INF/classes and WEB-INF/lib, respectively and in that order. These resources are only visible to the web application that loads them.
	The Common class loader loads all classes and JAR files contained in $CATALINA_HOME/lib. These resources are visible to all applications and to Tomcat. 
	There you have it. Rather than resolving one classpath configured in one attribute in the standard location for a Java application, Tomcat resolves multiple classpaths configured using 4 or more attributes, only one of which is configured in the standard location.
	


## 自定义类加载器
由于历史原因，系统采用了很早期的poi，而最新的poi和之前版本并不兼容,现在要系统要增加一个新功能，需要引入最新的jar文件,在不影响已有使用的基础上,我们如何处理该问题?
	
	思路是写一个类加载器，动态的加载所需的jar文件到一个单独的命名空间,
	由于jvm默认的类加载是采用父委托机制的，
	但在这里，类加载器的实现思路和一些web 容器的类加载机制是一致的(如tomcat jetty等) ，
	即优先加载自己指定路径下的jar文件，如果加载不到所需的类文件则委托给父加载器，
	所以我们需重写ClassLoader的loadClass方法。
	有一点比较重要,即默认java类所依赖的类是采用和该类相同的类加载器加载的。

#
	public class ParentLastClassLoader extends ClassLoader{
	   private String[] jarFiles; //jar文件路径
	   
	   private Hashtable classes = new Hashtable(); //将定义的类缓存在hashtable里面
	 
	   public ParentLastClassLoader(ClassLoader parent, String[] paths)
	   {
	       super(parent);
	       this.jarFiles = paths;
	   }
	 
	   @Override
	   public Class<?> findClass(String name) throws ClassNotFoundException
	   {
	       throw new ClassNotFoundException();
	   }
	 
	   @Override
	   protected synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException
	   {
	       try
	       {
	           byte classByte[];
	           Class result = null;
	           //先从缓存中查看是否已经加载该类
	           result = (Class) classes.get(className);
	           if (result != null) {
	               return result;
	           }
	          //如果没找到该类,则直接从jar文件里面加载
	           for(String jarFile: jarFiles){
	               try {
	                   JarFile jar = new JarFile(jarFile);
	                   JarEntry entry = jar.getJarEntry(className.replace(".","/") + ".class");
	                   InputStream is = jar.getInputStream(entry);
	                   ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	                   int nextValue = is.read();
	                   while (-1 != nextValue) {
	                       byteStream.write(nextValue);
	                       nextValue = is.read();
	                   }
	                   classByte = byteStream.toByteArray();
	                   if(classes.get(className) == null){
	                   result = defineClass(className, classByte, 0, classByte.length, null);
	                   classes.put(className, result);
	                   }
	               } catch (Exception e) {
	                   continue;
	               }
	           }
	           result = (Class) classes.get(className);
	           if (result != null) {
	               return result;
	           }
	           else{
	               throw new ClassNotFoundException("Not found "+ className);
	           }
	       }
	       catch( ClassNotFoundException e ){
	           return super.loadClass(className, resolve);
	       }
	   }
	}




