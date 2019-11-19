/*
Date: 04/25,2019, 20:13
*/
package netty.codec.fastjson;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FastJsonDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);

        System.out.println("decode " + new String(bytes));
        out.add(JSONObject.parseObject(bytes, User.class));
    }
}
