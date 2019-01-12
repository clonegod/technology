package clonegod.network.netty.rpc.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

import clonegod.network.netty.rpc.api.RPCMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * PRC 客户端服务调用代理 调用IRPCHello中的每个方法（非Object中的方法），实际上是发起了一次网络请求
 *
 */
public class RPCProxy implements InvocationHandler {
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<?> interfaceCls) {
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), 
				new Class[] {interfaceCls}, 
				new RPCProxy(interfaceCls));
	}

	private Class<?> targetCls; /** 被代理的接口 */
	
	private NettyClient nettyClient = new NettyClient(new RPCResponseHandler());
	
	public RPCProxy(Class<?> interfaceCls) {
		this.targetCls = interfaceCls;
		connectRemoteService();
	}
	

	private void connectRemoteService() {
		nettyClient = new NettyClient(new RPCResponseHandler());
		Executors.newSingleThreadExecutor().execute(() -> nettyClient.start());
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		System.out.println("Before ...");
		
		if (method.getDeclaringClass() == Object.class) {
			result = method.invoke(this, args); // hashcode equalse toString ... ?
		} else {
			// 每次方法调用，都启动一次netty客户端与服务器建立连接，发送消息，关闭 --- 低效
//			result = rpcInvoke(method, args);
			
			// netty客户端与服务器建立长连接，之后的每个方法调用都使用同一个netty客户端发送消息 --- 高效
			nettyClient.sendMsg(targetCls.getName(), method, args);
		}

		System.out.println("After ...");

		return result;
	}

	/**
	 * 同步调用。。。每次方法调用，都重新与服务器建立一次连接，很浪费资源！！
	 * 
	 * 应该把Netty客户端抽取出来，一个服务只需要启动一个netty客户端，通过长连接方式与服务器进行通信。
	 * 这样，一个方法被调用多次，也可以复用同一个已建立好的连接。
	 */
	private Object rpcInvoke(Method method, Object[] args) {
		final RPCResponseHandler responseHandler = new RPCResponseHandler();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 粘包拆包处理
							pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
							pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 4, 0, false)); // 头部声明消息的总长度

							// 编解码器
							pipeline.addLast("jdkEncoder", new ObjectEncoder());
							pipeline.addLast("jdkDecoder",
									new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

							// 业务处理
							pipeline.addLast(responseHandler);
						}
					});

			ChannelFuture cf = b.connect(new InetSocketAddress("localhost", 8000)).sync();

			RPCMsg rpcMsg = new RPCMsg(targetCls.getName(), method.getName(), method.getParameterTypes(), args);
			
			ChannelFuture cf2 = cf.channel().writeAndFlush(rpcMsg).sync();
			
			cf2.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		
		// 注意：这里采用的是等待连接关闭之后，从ChannelHandler中获取返回结果---实际上是模拟了一个同步调用的过程，与Netty异步非阻塞的IO模型相违背！
		return responseHandler.getResponseReulst();
	}

}