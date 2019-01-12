package part3_mop.methodinterceptor

import org.codehaus.groovy.runtime.InvokerHelper

/**
 * 实现 GroovyInterceptable 接口， 这是一个标记接口
 * 覆盖 invokeMethod 方法，为了保证我们能够拦截定义的所有方法
 * 
 * 该方式实现方法的拦截具有一定局限性：
 * 1、仅仅工作在 GroovyObject 对象上，不能是任意的 java 类
 * 2、目标类不能继承其它类，否则无法继承Traceable。
 */
class Traceable implements GroovyInterceptable {
	
	// default stdout
	Writer writer = new PrintWriter(System.out)
	private int indent = 0
	
	// 在 invokeMethod 方法内部，在执行方法调用前后写跟踪日志
	@Override
	Object invokeMethod(String methodName, Object args) {
		writer.write("\n" + " "*indent + "before method $methodName")
		writer.flush()
		indent++
		
		def metaClass = InvokerHelper.getMetaClass(this)
		def result = metaClass.invokeMethod(this, methodName, args)
		
		indent--
		writer.write("\n" + " "*indent + "after method $methodName")
		writer.flush()
		return result
	}
	
}
