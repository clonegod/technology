package clonegod.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import clonegod.rpc.api.RPCRequest;
import clonegod.rpc.client.servicediscovery.IServiceDiscovery;

public class RemoteInvocationHandler implements InvocationHandler {
	
	private IServiceDiscovery serviceDiscovery;
	private String version;

	public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
		this.serviceDiscovery = serviceDiscovery;
		this.version = version;
	}

	/**
	 * 代理对象被调用时，在invoke()中与服务端进行socket通信，将调用信息发送给服务端
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			// 构造远程传输的对象
			RPCRequest request = new RPCRequest();
			request.setClassName(method.getDeclaringClass().getName());
			request.setMethod(method.getName());
			request.setParams(args);
			request.setVersion(version);
			
			// 启动socket连接
			String serviceName = request.getClassName();
			if(this.version != null && !"".equals(version)) {
				serviceName = serviceName + "-" + request.getVersion();
			}
			String serviceAddress = serviceDiscovery.discovery(serviceName);
			String host = serviceAddress.split(":")[0];
			int port = Integer.parseInt(serviceAddress.split(":")[1]);
			TCPTransport tcp = new TCPTransport(host, port);
			
			// 通过socket发送请求对象
			result = tcp.send(request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
