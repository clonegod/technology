package clonegod.network.netty.rpc.consumer;

import java.util.stream.IntStream;

import clonegod.network.netty.rpc.api.IRPCHello;

public class ConsumerMain {
	
	public static void main(String[] args) {
		IRPCHello hello = RPCProxy.createProxy(IRPCHello.class);
		
//		System.err.println(hello.toString());
		
//		System.out.println(hello.sayHello("Alice"));
		
		IntStream.range(1, 3).forEach(n -> hello.sayHello("Alice"+n));
		
		hello.sayHello(null);
	}
	
}
