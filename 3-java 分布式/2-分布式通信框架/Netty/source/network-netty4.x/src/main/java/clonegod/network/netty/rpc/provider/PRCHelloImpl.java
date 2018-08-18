package clonegod.network.netty.rpc.provider;

import clonegod.network.netty.rpc.api.IRPCHello;

public class PRCHelloImpl implements IRPCHello {

	@Override
	public String sayHello(String name) {
		return "Hello: " + name;
	}
	
}
