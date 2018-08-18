package clonegod.network.netty.tcp.client;

import clonegod.network.netty.tcp.api.RequestData;
import clonegod.network.netty.tcp.api.ResponseData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
  
	/**
	 * 连接建立成功，客户端向服务器发送数据
	 */
    @Override
    public void channelActive(ChannelHandlerContext ctx) 
      throws Exception {
  
        RequestData msg = new RequestData();
        msg.setIntValue(123);
        msg.setStringValue(
          "all work and no play makes jack a dull boy");
        ctx.writeAndFlush(msg);
        
    }
 
    /**
     * 接收服务器返回的数据，关闭channel
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) 
      throws Exception {
        System.out.println((ResponseData)msg);
        ctx.close();
    }

}