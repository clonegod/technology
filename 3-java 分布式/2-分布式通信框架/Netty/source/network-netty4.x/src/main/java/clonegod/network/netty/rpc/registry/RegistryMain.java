package clonegod.network.netty.rpc.registry;

import java.nio.ByteOrder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 服务注册中心（接收客户端请求，返回服务地址） 
 *	
 */
public class RegistryMain {
	private int port;
	
	public RegistryMain(int port) {
		this.port = port;
		ServiceRegistry.registry(null);
	}
	
	public void start() {
		EventLoopGroup parentGroup = new NioEventLoopGroup(); 
		EventLoopGroup childGroup = new NioEventLoopGroup();
		
		try {
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(parentGroup, childGroup)
			 .channel(NioServerSocketChannel.class)
			 .childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					// 粘包拆包处理
					pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 4, 0, false)); // 头部声明消息的总长度
					
					// 编解码器
					pipeline.addLast("jdkEncoder", new ObjectEncoder());
					pipeline.addLast("jdkDecoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
					
					// 业务处理
					pipeline.addLast(new RPCRequestHandler());
				}
			})
			 .option(ChannelOption.SO_BACKLOG, 128)
			 .childOption(ChannelOption.SO_KEEPALIVE, true);
			
			
			// 阻塞等待服务器启动
			ChannelFuture cf = b.bind(port).sync();
			
			System.out.println("ServiceRegistry start listening at: " + port);
			
			// 阻塞线程，避免线程退出导致服务器关闭
			cf.channel().closeFuture().sync();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
		
	}

	public static void main(String[] args) {
		int port = 8000;
		new RegistryMain(port).start();
	}
}
