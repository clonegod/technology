### ClassNotFoundException vs NoClassDefFoundError

## ClassNotFoundException
	出现在Runtime阶段，一般都是通过反射创建类实例时，类加载器找不到指定的类文件导致的。

	举例：
	@Test(expected = ClassNotFoundException.class)
	public void givenNoDrivers_whenLoadDriverClass_thenClassNotFoundException() 
	  throws ClassNotFoundException {
	      Class.forName("oracle.jdbc.driver.OracleDriver"); // 反射，但是classpath下没有这个类的字节码文件存在
	}

## NoClassDefFoundError
	编译阶段class文件是存在的，但是Runtime阶段，类加载却找不到这个class的定义。

	举例：
	public class ClassWithInitErrors {
	    static int data = 1 / 0; // 初始化阶段发生异常，导致类没有被正确加载到JVM
	}

	public class NoClassDefFoundErrorExample {
	    public ClassWithInitErrors getClassWithInitErrors() {
	        ClassWithInitErrors test;
	        try {
	            test = new ClassWithInitErrors();
	        } catch (Throwable t) {
	            System.out.println(t); // ExceptionInInitializerError
	        }
	        test = new ClassWithInitErrors(); // -> 抛出NoClassDefFoundError
	        return test;
	    }
	}

	@Test(expected = NoClassDefFoundError.class)
	public void givenInitErrorInClass_whenloadClass_thenNoClassDefFoundError() {
	  
	    NoClassDefFoundErrorExample sample
	     = new NoClassDefFoundErrorExample();
	    sample.getClassWithInitErrors();
	}