package clonegod.network.netty.tcp.server;

import clonegod.network.netty.tcp.api.RequestData;
import clonegod.network.netty.tcp.api.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  Every method of our handler is called when its corresponding event occurs.
 *
 */
public class SimpleProcessingHandler extends ChannelInboundHandlerAdapter {
	private ByteBuf tmp;
	final private int msgByteCount = 4; // 1个int 占 4个byte
	
	/**
	 * So we initialize the buffer when the handler is added, 
	 * fill it with data on receiving new bytes and start processing it when we get enough data.
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		System.out.println("Handler added");
		tmp = ctx.alloc().buffer(msgByteCount);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		System.out.println("Handler removed");
		tmp.release();
		tmp = null;
	}

	/**
	 * 直接操作底层ByteBuf，没有使用解码器。
	 * 关键是；需要判断是否所有数据都已经接收完毕
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf m = (ByteBuf) msg;
		tmp.writeBytes(m);
		m.release();
		// create a temporary ByteBuf and append to it all inbound bytes until we get the required amount of bytes
		if (tmp.readableBytes() >= msgByteCount) {
			// request processing
			RequestData requestData = new RequestData();
			requestData.setIntValue(tmp.readInt());
			
			// 计算响应结果
			ResponseData responseData = new ResponseData();
			responseData.setIntValue(requestData.getIntValue() * 2);
			
			// 响应数据成功后，关闭连接
			ChannelFuture future = ctx.writeAndFlush(responseData);
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
}