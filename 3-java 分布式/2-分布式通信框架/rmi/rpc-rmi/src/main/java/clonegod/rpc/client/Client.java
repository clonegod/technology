package clonegod.rpc.client;

import java.util.stream.IntStream;

import clonegod.rpc.api.UserService;
import clonegod.rpc.client.servicediscovery.IServiceDiscovery;
import clonegod.rpc.client.servicediscovery.ZkServiceDiscovery;
import clonegod.rpc.server.serviceregistry.ZkConfig;

public class Client {
	
	public static void main(String[] args) {
		
		// 连接注册中心
		IServiceDiscovery serviceDiscovery = new ZkServiceDiscovery(ZkConfig.CONNECTION_STR);
		
		// 创建客户端代理对象
		PRCClientProxy clientProxy = new PRCClientProxy(serviceDiscovery);
		
		IntStream.range(1, 20).forEach(n ->  {
			// 返回代理对象
			UserService userService = clientProxy.create(UserService.class, null);
//			UserService userService = clientProxy.create(UserService.class, "2.0");
			// 代理对象的invoke方法，会从注册中心获取服务地址，透明地完成远程调用并返回结果
			String response = userService.echo("rmi use socket to communicate between server and client!");
			System.out.println(response);
		});
		
	}
}
