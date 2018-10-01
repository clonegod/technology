package clonegod.rpc.client;

import java.lang.reflect.Proxy;

public class PRCClientProxy {

	/**
	 * 为远程接口创建代理对象
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> interfaceCls, String ip, int port) {
		
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), 
						new Class[] {interfaceCls}, 
						new RemoteInvocationHandler(ip, port));
	}

}
