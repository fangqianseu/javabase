/*
Date: 04/29,2019, 15:33
*/
package netty.protocol.netty.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.protocol.netty.struct.NettyMessage;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode message is null");

        // 写入头部信息
        out.writeInt(msg.getHeader().getCrcCode());
        out.writeInt(msg.getHeader().getLength());
        out.writeLong(msg.getHeader().getSessionID());
        out.writeByte(msg.getHeader().getType());
        out.writeByte(msg.getHeader().getPriority());

        // 写入attachment信息
        String attachmentstr = JSONObject.toJSONString(msg.getHeader().getAttachment());
        ByteBuf attachment = Unpooled.wrappedBuffer(attachmentstr.getBytes());
        int attachmentLength = attachment.readableBytes();
        if (attachmentLength > 0) {
            out.writeInt(attachmentLength);
            out.writeBytes(attachment);
        } else {
            out.writeInt(0);
        }
        // 写入 body 信息
        String bodystr = JSONObject.toJSONString(msg.getBody());
        ByteBuf body = Unpooled.wrappedBuffer(bodystr.getBytes());
        int bodyLength = body.readableBytes();
        if (bodyLength > 0) {
            out.writeInt(bodyLength);
            out.writeBytes(body);
        } else {
            out.writeInt(0);
        }
    }
}
