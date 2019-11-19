/*
Date: 04/27,2019, 09:54
*/
package netty.protocol.http.json.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HttpJsonRequestEncoder extends MessageToMessageEncoder<HttpJsonRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws Exception {
        ByteBuf body = Unpooled.copiedBuffer(JSONObject.toJSONString(msg.getBody()).getBytes());
        System.out.println("resquestencoder: " + body.toString(CharsetUtil.UTF_8));

        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, "/", body);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaderNames.HOST, "127.0.0.1");
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        }
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());

        out.add(request);
    }
}
