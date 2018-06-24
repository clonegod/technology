package clonegod.network.netty.tcp.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyTCPClient {
	
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 8000;
		EventLoopGroup group = new NioEventLoopGroup();
		try{
		    Bootstrap b = new Bootstrap();
		    b.group(group);
		    b.channel(NioSocketChannel.class);
		    b.remoteAddress(new InetSocketAddress(host, port));
		    b.handler(new ChannelInitializer<SocketChannel>() {
		        protected void initChannel(SocketChannel sc) throws Exception {
		            sc.pipeline().addLast(new RequestDataEncoder());
		            sc.pipeline().addLast(new ResponseDataDecoder());
		            sc.pipeline().addLast(new ClientHandler());
		        }
		    });
		    
		    ChannelFuture channelFuture = b.connect().sync();
		    
		    channelFuture.channel().closeFuture().sync();
		} finally {
		    group.shutdownGracefully().sync();
		}
	}
}
