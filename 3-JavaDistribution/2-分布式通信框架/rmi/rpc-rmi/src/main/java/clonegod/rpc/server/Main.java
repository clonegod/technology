package clonegod.rpc.server;

import java.net.InetAddress;

import clonegod.rpc.server.serviceregistry.IRegistryCenter;
import clonegod.rpc.server.serviceregistry.ZKRegistryCenter;

public class Main {

	/**
	 * 将本地服务注册到注册中心
	 */
	public static void main(String[] args) throws Exception {
		IRegistryCenter registryCenter = new ZKRegistryCenter();
		
		String ip = InetAddress.getLocalHost().getHostAddress();
		int port = 8080;
		RPCServer server = new RPCServer(registryCenter, ip + ":" + port);
		
		// 服务端支持注册多个服务到注册中心
		server.bind(new UserServiceImpl(), new UserServiceImplV2());
		
		server.publish();
		
		System.in.read();
	}
	
}
