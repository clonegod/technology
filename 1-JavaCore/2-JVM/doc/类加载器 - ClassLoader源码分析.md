## ClassLoader 加载类的主要源码分析

### loadClass() 内部对双亲委派逻辑的实现

Loads the class with the specified binary name. The default implementation of this method searches for classes in the following order: 
	
	1、Invoke findLoadedClass(String) to check if the class has already been loaded. 
	
	2、Invoke the loadClass method on the parent class loader. If the parent is null the class loader built-in to the virtual machine is used, instead. 
	
	3、Invoke the findClass(String) method to find the class if class still not loaded. 



#

	public abstract class ClassLoader {
	
	
	    // The parent class loader for delegation
	    private final ClassLoader parent;
	
	    
	    // The classes loaded by this class loader. The only purpose of this table
	    // is to keep the classes from being GC'ed until the loader is GC'ed.
	    // 这里使用一个Vector集合保存该ClassLoader加载的所有class，唯一的目的就是：在该ClassLoader被GC回收时，同时也将这些Class一起回收掉。
	    private final Vector<Class<?>> classes = new Vector<>();
	
	    protected ClassLoader(ClassLoader parent) {
	        this(checkCreateClassLoader(), parent);
	    }
	
	    
	    protected ClassLoader() {
	        this(checkCreateClassLoader(), getSystemClassLoader());
	    }    
	    
	    public Class<?> loadClass(String name) throws ClassNotFoundException {
	        return loadClass(name, false);
	    }
	
	    /**
	     * 先检查是否已经被加载过，
	     *     若没有加载则调用父类加载器的loadClass方法，
	     *     若父类加载器不存在，则使用启动类加载器。
	     * 如果父类加载器加载失败，则抛出异常之后看，再调用自己的findClass方法进行加载。
	     * 最后，返回加载到类
	     */
	    protected Class<?> loadClass(String name, boolean resolve)
	        throws ClassNotFoundException
	    {
	        synchronized (getClassLoadingLock(name)) {
	            // First, check if the class has already been loaded
	            // 1、检查类是否已经被加载
	            Class<?> c = findLoadedClass(name); 
	            // 2、该类还没有被加载过，按双亲委派机制加载类
	            if (c == null) { 
	                long t0 = System.nanoTime();
	                try {
	                    // 3、判断是否存在parent classloader，存在parent，则委托给parent去加载
	                    if (parent != null) { 
	                        c = parent.loadClass(name, false);
	                    } else {
	                        // 父类加载器为空，则使用启动类加载器
	                        c = findBootstrapClassOrNull(name);
	                    }
	                } catch (ClassNotFoundException e) {
	                    // ClassNotFoundException thrown if class not found
	                    // from the non-null parent class loader
	                }
	                
	                // 如果上述操作结束后，类仍然没有加载成功，则调用findClass() 
	                if (c == null) {
	                    // If still not found, then invoke findClass in order
	                    // to find the class.
	                    long t1 = System.nanoTime();
	                    // 如果父类加载失败，则使用自己的findClass方法进行加载
	                    c = findClass(name); 
	
	                    // this is the defining class loader; record the stats
	                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
	                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
	                    sun.misc.PerfCounter.getFindClasses().increment();
	                }
	            }
	            
	            // 是否对加载的类进行link（验证、准备、初始化）
	            if (resolve) {
	                resolveClass(c); // 对class进行link操作，如果已经被link，则直接返回
	            }
	            // 返回加载的class
	            return c;
	        }
	    }    
	    
	    
	    /**
	     * 子类需要重写该方法，提供查找类的逻辑
	     * Finds the class with the specified <a href="#name">binary name</a>.
	     * This method should be overridden by class loader implementations that
	     * follow the delegation model for loading classes, and will be invoked by
	     * the {@link #loadClass <tt>loadClass</tt>} method after checking the
	     * parent class loader for the requested class.  The default implementation
	     * throws a <tt>ClassNotFoundException</tt>.
	     *
	     * @param  name
	     *         The <a href="#name">binary name</a> of the class
	     *
	     * @return  The resulting <tt>Class</tt> object
	     *
	     * @throws  ClassNotFoundException
	     *          If the class could not be found
	     *
	     * @since  1.2
	     */
	    protected Class<?> findClass(String name) throws ClassNotFoundException {
	        throw new ClassNotFoundException(name);
	    }
	
	
	
	    /**
	     * 接受由原始字节组成的数组并把它转换成 Class 对象。
	     * Converts an array of bytes into an instance of class <tt>Class</tt>.
	     * Before the <tt>Class</tt> can be used it must be resolved.
	     *
	     * @param  name
	     *         The expected <a href="#name">binary name</a> of the class, or
	     *         <tt>null</tt> if not known
	     *
	     * @param  b
	     *         The bytes that make up the class data.  The bytes in positions
	     *         <tt>off</tt> through <tt>off+len-1</tt> should have the format
	     *         of a valid class file as defined by
	     *         <cite>The Java&trade; Virtual Machine Specification</cite>.
	     *
	     * @param  off
	     *         The start offset in <tt>b</tt> of the class data
	     *
	     * @param  len
	     *         The length of the class data
	     *
	     * @return  The <tt>Class</tt> object that was created from the specified
	     *          class data.
	     *
	     *
	     * @since  1.1
	     */
	    protected final Class<?> defineClass(String name, byte[] b, int off, int len)
	        throws ClassFormatError
	    {
	        return defineClass(name, b, off, len, null);
	    }    
	    
	    // Java 提供的SPI 机制，在BootstrapClassLoader中需要使用到ApplicaitonClassLoader所加载到的类
	    // 就是在这里将ApplicaitonClassLoader初始化到contextClassLoader中进行保存的
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
	                    scl = AccessController.doPrivileged(
	                        // SystemClassLoaderAction 的 run() 会在doPrivileged内部进行回调操作，从而将SystemClassLoader设置到contextClassLoader中
	                        new SystemClassLoaderAction(scl));
	                } catch (PrivilegedActionException pae) {
	                    oops = pae.getCause();
	                    if (oops instanceof InvocationTargetException) {
	                        oops = oops.getCause();
	                    }
	                }
	                if (oops != null) {
	                    if (oops instanceof Error) {
	                        throw (Error) oops;
	                    } else {
	                        // wrap the exception
	                        throw new Error(oops);
	                    }
	                }
	            }
	            sclSet = true;
	        }
	    }
	    
	    
	class SystemClassLoaderAction
	    implements PrivilegedExceptionAction<ClassLoader> {
	    private ClassLoader parent;
	
	    SystemClassLoaderAction(ClassLoader parent) {
	        this.parent = parent;
	    }
	
	    public ClassLoader run() throws Exception {
	        String cls = System.getProperty("java.system.class.loader");
	        if (cls == null) {
	            return parent;
	        }
	
	        Constructor<?> ctor = Class.forName(cls, true, parent)
	            .getDeclaredConstructor(new Class<?>[] { ClassLoader.class });
	        ClassLoader sys = (ClassLoader) ctor.newInstance(
	            new Object[] { parent });
			// 将SystemClassLoader设置为ContextClassLoader
	        Thread.currentThread().setContextClassLoader(sys); 
	        return sys;
	    }
	}    
	    
	}	
