package clonegod.rpc.server;

import clonegod.rpc.api.UserService;
import clonegod.rpc.server.anno.RPCAnnotation;

@RPCAnnotation(value=UserService.class, version="2.0")
public class UserServiceImplV2 implements UserService {

	@Override
	public String echo(String msg) {
		return "[version-2.0]Message From Server: " + msg;
	}

}
