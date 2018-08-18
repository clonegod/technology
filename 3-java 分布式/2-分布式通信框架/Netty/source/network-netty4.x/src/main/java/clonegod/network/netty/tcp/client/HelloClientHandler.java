package clonegod.network.netty.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class HelloClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * 连接建立成功后，发送消息给服务端
	 */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
    }

    /**
     * 处理服务端返回的消息
     */
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) {
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
    	// Close the connection when an exception is raised.
        cause.printStackTrace();
        channelHandlerContext.close();
    }



}