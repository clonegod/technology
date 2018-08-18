package clonegod.network.netty.tcp.server;

import java.nio.charset.Charset;
import java.util.List;

import clonegod.network.netty.tcp.api.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * Netty provides useful decoder classes which are implementations of ChannelInboundHandler: 
 * 	ByteToMessageDecoder and ReplayingDecoder.
 *	
 *	ReplayingDecoder - 内部重试、直到所有数据达到缓冲区（内部通过抛异常来通知数据未全部到达，捕获异常后会在下次读事件发生时重新读取数据）
 *		An idea of this decoder is pretty simple. 
 *		It uses an implementation of ByteBuf which throws an exception when there is not enough data in the buffer for the reading operation.
 */
public class RequestDecoder extends ReplayingDecoder<RequestData> {

	private final Charset charset = Charset.forName("UTF-8");
	
	/**
	 * When the exception is caught the buffer is rewound to the beginning and the decoder waits for a new portion of data. 
	 * Decoding stops when the out list is not empty after decode execution.
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		RequestData data = new RequestData();
		data.setIntValue(in.readInt());
		int strLen = in.readInt();
		data.setStringValue(in.readCharSequence(strLen, charset).toString());
		out.add(data);
	}
}