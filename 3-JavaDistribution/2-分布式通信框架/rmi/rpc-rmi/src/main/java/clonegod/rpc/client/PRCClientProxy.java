package clonegod.rpc.client;

import java.lang.reflect.Proxy;

import clonegod.rpc.client.servicediscovery.IServiceDiscovery;

public class PRCClientProxy {

	// 从注册中心发现服务地址
	private IServiceDiscovery serviceDiscovery;
	
	public PRCClientProxy(IServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}


	/**
	 * 为远程接口创建代理对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> interfaceCls, String version) {
		
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), 
						new Class[] {interfaceCls}, 
						new RemoteInvocationHandler(serviceDiscovery, version));
	}

}
