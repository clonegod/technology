package clonegod.network.netty.rpc.registry;

import java.lang.reflect.Method;

import clonegod.network.netty.rpc.api.RPCMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RPCRequestHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("New msg: " + msg.toString());
		Object result = new Object();
		
		try {
			RPCMsg rpcMessage = (RPCMsg)msg;
			
			// 从注册中心获取服务
			Object serviceInstance = ServiceRegistry.registryMap.get(rpcMessage.getClassName());
			if(serviceInstance != null) {
				Method method = serviceInstance.getClass()
						.getDeclaredMethod(rpcMessage.getMethodName(), rpcMessage.getParameterTypes());
				
				result = method.invoke(serviceInstance, rpcMessage.getParameterValues());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			ctx.writeAndFlush(result);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
