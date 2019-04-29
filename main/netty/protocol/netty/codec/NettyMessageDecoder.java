/*
Date: 04/29,2019, 15:54
*/
package netty.protocol.netty.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import netty.protocol.netty.struct.Header;
import netty.protocol.netty.struct.NettyMessage;

import java.util.HashMap;
import java.util.List;

public class NettyMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg == null)
            return;
        NettyMessage message = new NettyMessage();

        // 构造 header 注意编码顺序
        Header header = new Header();
        header.setCrcCode(msg.readInt());
        header.setLength(msg.readInt());
        header.setSessionID(msg.readLong());
        header.setType(msg.readByte());
        header.setPriority(msg.readByte());
        message.setHeader(header);

        int attachmentLength = msg.readInt();
        if (attachmentLength > 0) {
            byte[] attachmentbytes = new byte[attachmentLength];
            msg.readBytes(attachmentbytes);
            HashMap map = JSONObject.parseObject(new String(attachmentbytes, CharsetUtil.UTF_8), HashMap.class);
            header.setAttachment(map);
        }

        int bodyLength = msg.readInt();
        if (bodyLength > 0) {
            byte[] bodybytes = new byte[bodyLength];
            msg.readBytes(bodybytes);
            Object body = JSONObject.parseObject(new String(bodybytes, CharsetUtil.UTF_8), Object.class);
            message.setBody(body);
        }
        out.add(message);
    }
}
