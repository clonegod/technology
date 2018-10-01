package clonegod.rpc.server;

import java.net.InetAddress;

import clonegod.rpc.api.UserService;
import clonegod.rpc.server.serviceregistry.IRegistryCenter;
import clonegod.rpc.server.serviceregistry.ZKRegistryCenter;

public class LoadBalancerMain {

	/**
	 * 将本地服务注册到注册中心
	 */
	public static void main(String[] args) throws Exception {
		IRegistryCenter registryCenter = new ZKRegistryCenter();
		
		String ip = InetAddress.getLocalHost().getHostAddress();
		
		Thread server1 = new Thread(new Runnable() {
			@Override
			public void run() {
				RPCServer server1 = new RPCServer(registryCenter, ip + ":" + 8088);
				server1.bind(new UserServiceImpl());
				server1.publish();
			}
		}, "server1");
		
		Thread server2 = new Thread(new Runnable() {
			@Override
			public void run() {
				RPCServer server2 = new RPCServer(registryCenter, ip + ":" + 8089);
				server2.bind(new UserServiceImpl());
				server2.publish();
			}
		}, "server2");
		
		server1.start();
		server2.start();
		
		server1.join();
		server2.join();
	}
	
}
