package clonegod.network.netty.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * The channelRead() method is called whenever data is received from the SocketChannel
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf inBuffer = (ByteBuf) msg;

		String received = inBuffer.toString(CharsetUtil.UTF_8);
		System.out.printf("[%s]Server received: %s\n",
				Thread.currentThread().getName(), received);
		ctx.write(Unpooled.copiedBuffer("Hello " + received, CharsetUtil.UTF_8));
	}

	/**
	 * The channelReadComplete() method is called when there is no more data to read from the SocketChannel.
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
			.addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * The exceptionCaught() method is called if an exception is thrown 
	 * while receiving or sending data from the SocketChannel. 
	 * 
	 * In here you can decide what should happen, 
	 * like closing the connection, or responding with an error code etc.
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
