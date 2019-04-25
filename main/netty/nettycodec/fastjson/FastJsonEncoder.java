/*
Date: 04/25,2019, 20:10
*/
package netty.nettycodec.fastjson;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class FastJsonEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("encode " + msg);
        out.writeBytes(JSONObject.toJSONString((User) msg).getBytes());
    }
}
