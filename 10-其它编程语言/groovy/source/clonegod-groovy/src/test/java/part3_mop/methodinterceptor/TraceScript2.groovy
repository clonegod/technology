package part3_mop.methodinterceptor

import org.codehaus.groovy.runtime.StringBufferWriter

/**
 * Intercepting method calls with ProxyMetaClass and TracingInterceptor
 * 
 * 注意这个解决方案在从 groovy 中调用 java 类的时候也可用!
 */
part3_mop.methodinterceptornew TracingInterceptor()
def log = new StringBuffer("\n")
tracer.writer = new StringBufferWriter(log)

def proxy = ProxyMetaClass.getInstance(WhatEver.class)
proxy.interceptor = tracer
proxy.use { 
	assert 1 == new WhatEver().outer()
}

println log.toString()
