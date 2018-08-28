## 反射-Constructor

## 1、反射无参构造方法
	Class.forName("className").newInstance() 
always invokes no argument default constructor.

## 2、反射带参数的构造方法
To invoke parametrized constructor instead of zero argument no-arg constructor,

	  1. You have to get Constructor with parameter types by passing types in Class[] for getDeclaredConstructor method of Class
	  2. You have to create constructor instance by passing values in Object[] for newInstance method of Constructor

##Example code:
	import java.lang.reflect.*;
	
	class NewInstanceWithReflection{
	    public NewInstanceWithReflection(){
	        System.out.println("Default constructor");
	    }
	    public NewInstanceWithReflection( String a){
	        System.out.println("Constructor :String => "+a);
	    }
	    public static void main(String args[]) throws Exception {	
			// 反射无参构造函数
	        NewInstanceWithReflection object = (NewInstanceWithReflection)Class.forName("NewInstanceWithReflection").newInstance();

			// 反射带参数的构造函数
	        Constructor constructor = NewInstanceWithReflection.class.getDeclaredConstructor( new Class[] {String.class});

			// 调用带参构造函数，传入参数，创建对象
	        NewInstanceWithReflection object1 = (NewInstanceWithReflection)constructor.newInstance(new Object[]{"StackOverFlow"});
	    }
	}

#
	output:
	java NewInstanceWithReflection
	Default constructor
	Constructor :String => StackOverFlow