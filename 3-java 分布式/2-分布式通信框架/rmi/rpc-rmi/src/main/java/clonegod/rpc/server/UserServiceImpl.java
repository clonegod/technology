package clonegod.rpc.server;

import clonegod.rpc.api.UserService;

public class UserServiceImpl implements UserService {

	@Override
	public String echo(String msg) {
		return "Message From Server: " + msg;
	}

}
