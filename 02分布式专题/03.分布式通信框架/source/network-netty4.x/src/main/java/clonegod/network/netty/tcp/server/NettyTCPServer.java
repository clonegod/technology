package clonegod.network.netty.tcp.server;

import java.util.Arrays;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTCPServer {
	
	private int port;
	
	public NettyTCPServer(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception {
		new NettyTCPServer(8000).start();
	}
	
	/** 单线程模型 */
	EventLoopGroup[] singleThreadGroup() {
		// bossGroup 和 workerGroup 都是同一个，并且内部线程池只有1个线程===即所有事件都由1个线程来执行
		EventLoopGroup group = new NioEventLoopGroup(1);
		return new EventLoopGroup[] {group, group};
	}
	
	/** 多线程模型 */
	EventLoopGroup[] multipleThreadGroup() {
		// bossGroup 和 workerGroup 都是同一个，但内部线程池只有N个线程===即由一个共享的线程池来处理Accept事件和处理Read事件
		EventLoopGroup group = new NioEventLoopGroup(3);
		return new EventLoopGroup[] {group, group};
	}
	
	/** 主从线程模型 - 推荐，性能最好 */
	EventLoopGroup[] bossWorkerThreadGroup() {
		// bossGroup 和 workerGroup 分别是不同的线程池，bossGroup只处理Accept事件完成连接的建立，读写事件由workerGroup来处理
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		return new EventLoopGroup[] {bossGroup, workerGroup};
	}
	
	
	private void start() throws Exception {
		EventLoopGroup[] groups = bossWorkerThreadGroup(); 
		try {
			ServerBootstrap serverBootStrap = new ServerBootstrap();
			serverBootStrap
				// 配置Netty采用的线程模型
				.group(groups[0], groups[1])
				.channel(NioServerSocketChannel.class)
//				.localAddress(new InetSocketAddress(ip, port))
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				
				// each accepted SocketChannel is considered a "child" of the server socket that accepts it.
				.childHandler(new ChannelInitializer<SocketChannel>() {
					// The ChannelInitializer initializes the sockets of all incoming TCP connections.
					// initChannel() is called whenever a new incoming TCP connection is accepted by the TCP server. 
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast(new RequestDecoder());
						sc.pipeline().addLast(new ResponseDataEncoder());
						sc.pipeline().addLast(new ProcessingHandler());
					}
				});
			
			// Bind and start to accept incoming connections.
			ChannelFuture channelFuture = serverBootStrap.bind(port).sync();
			
			System.out.printf("[%s] Netty Server listening at %d\n", 
					Thread.currentThread().getName(),  
					this.port);
			
			channelFuture.channel().closeFuture().sync();
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			Arrays.stream(groups).forEach(g -> {
				if(! g.isShutdown() ) {
					try {
						g.shutdownGracefully().sync();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
