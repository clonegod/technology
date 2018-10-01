package clonegod.rpc.server;

import clonegod.rpc.api.UserService;

public class Main {

	public static void main(String[] args) {
		RPCServer server = new RPCServer(12345);
		
		UserService userService = new UserServiceImpl();
		
		server.publish(userService);
	}
	
}
