package clonegod.network.netty.tcp.server;

import clonegod.network.netty.tcp.api.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Besides decoding the RequestData we need to encode the message. 
 * This operation is simpler because we have the full message data when the write operation occurs.
 * 
 * We can write data to Channel in our main handler 
 * or we can separate the logic and create a handler extending MessageToByteEncoder 
 * which will catch the write ResponseData operation:
 *
 */
public class ResponseDataEncoder extends MessageToByteEncoder<ResponseData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
		out.writeInt(msg.getIntValue());
	}
}