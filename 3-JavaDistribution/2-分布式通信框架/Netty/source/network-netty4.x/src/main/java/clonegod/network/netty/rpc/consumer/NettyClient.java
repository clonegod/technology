package clonegod.network.netty.rpc.consumer;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

import clonegod.network.netty.rpc.api.RPCMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
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

public class NettyClient {
	
	private EventLoopGroup group;
	
	private ChannelHandler channelHandler;
	
	private ChannelFuture channelFuture;
	
	public NettyClient(ChannelHandler channelHandler) {
		this.group = new NioEventLoopGroup();
		this.channelHandler = channelHandler;
	}

	public void start() {
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
							pipeline.addLast(channelHandler);
						}
					});

			this.channelFuture = b.connect(new InetSocketAddress("localhost", 8000)).sync();
			System.out.println("连接服务器成功！");
			
			// 让启动netty客户端的线程被阻塞，直到channel被关闭
			this.channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void sendMsg(String className, Method method, Object[] args) {
		
		if(args[0] == null) {
			System.out.println("参数为空，关闭与服务器的连接");
			try {
				TimeUnit.SECONDS.sleep(1);
				this.channelFuture.channel().close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		RPCMsg rpcMsg = new RPCMsg(className, method.getName(), method.getParameterTypes(), args);
		try {
			if(this.channelFuture == null) {
				TimeUnit.SECONDS.sleep(1);
			}
			this.channelFuture.channel().writeAndFlush(rpcMsg)/*.sync()*/;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		System.out.println("[closing]客户端正在关闭与服务器的连接");
		try {
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("[closed]客户端关闭与服务器的连接成功");
	}
}
