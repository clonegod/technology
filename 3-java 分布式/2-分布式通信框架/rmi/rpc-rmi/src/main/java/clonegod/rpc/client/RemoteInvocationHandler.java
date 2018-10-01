package clonegod.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import clonegod.rpc.api.RPCRequest;

public class RemoteInvocationHandler implements InvocationHandler {
	
	private String host;
	private int port;
	
	public RemoteInvocationHandler(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	/**
	 * 代理对象被调用时，在invoke()中与服务端进行socket通信，将调用信息发送给服务端
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		try {
			TCPTransport tcp = new TCPTransport(host, port);
			
			// 构造远程传输的对象
			RPCRequest request = new RPCRequest();
			request.setClassName(method.getDeclaringClass().getName());
			request.setMethod(method.getName());
			request.setParams(args);
			
			// 通过socket发送请求对象
			result = tcp.send(request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
