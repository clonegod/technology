package clonegod.rpc.server;

import clonegod.rpc.api.UserService;
import clonegod.rpc.server.anno.RPCAnnotation;

@RPCAnnotation(value=UserService.class)
public class UserServiceImpl implements UserService {

	@Override
	public String echo(String msg) {
		return this.hashCode() + " Message From Server: " + msg;
	}

}
