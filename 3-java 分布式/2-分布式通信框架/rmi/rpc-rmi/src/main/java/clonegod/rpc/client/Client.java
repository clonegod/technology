package clonegod.rpc.client;

import clonegod.rpc.api.UserService;

public class Client {
	
	public static void main(String[] args) {
		
		// 返回代理对象
		UserService userService = PRCClientProxy.createProxy(UserService.class, "localhost", 12345);
		
		// 代理对象完成远程调用
		String response = userService.echo("rmi use socket to communicate between server and client!");
		
		System.out.println(response);
	}
}
