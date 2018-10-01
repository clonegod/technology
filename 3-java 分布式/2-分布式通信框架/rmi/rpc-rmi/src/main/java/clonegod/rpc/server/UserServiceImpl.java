package clonegod.rpc.server;

import clonegod.rpc.api.UserService;
import clonegod.rpc.server.anno.RPCAnnotation;

@RPCAnnotation(value=UserService.class, version="1.0")
public class UserServiceImpl implements UserService {

	@Override
	public String echo(String msg) {
		return this.hashCode() + " - [version-1.0] Message From Server: " + msg;
	}

}
