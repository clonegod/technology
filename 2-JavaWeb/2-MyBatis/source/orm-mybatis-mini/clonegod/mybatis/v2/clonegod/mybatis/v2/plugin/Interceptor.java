package clonegod.mybatis.v2.plugin;

import clonegod.mybatis.v2.plugin.proxy.Invocation;

/**
 * plugin是基于拦截器做的，因此先声明一个接口作为Interceptor
 * 什么是拦截器呢？
 * 	拦截器就是在某个类的某个方法被执行的时候，在执行之前，或者之后进行某种操作
 * 
 * 
 * 怎样实现这个功能：凡是实现了Intercepter的类(插件)都被执行到，在流程的哪个地方被执行？
 * 1、按官方的文档说明，拦截器可以拦截的点包括：Executor，ParameterHandler ，ResultSetHandler ，StatementHandler 
 * 2、要让plugin被执行，因此需要在4个地方进行埋点，即把plugin或者是plugin的代理‘注入’进去。
 * 	比如注入plugin的代理到Executor中，当Executor的代码执行sql前，就去调用这个代理对象，从而实现plugin的执行。
 * 
 * @author clonegod
 *
 */
public interface Interceptor {
	  
	/**
	 * 
	 * @param invocation 封装了：目标对象，方法，参数
	 * @return	方法执行的结果
	 */
	  Object intercept(Invocation invocation) throws Throwable;

	  /**
	   * 
	   * @param target	需要被代理的对象
	   * @return	生成的代理对象
	   */
	  Object plugin(Object target);

//	  void setProperties(Properties properties);
}
