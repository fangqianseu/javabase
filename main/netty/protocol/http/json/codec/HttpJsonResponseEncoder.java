/*
Date: 04/27,2019, 10:52
*/
package netty.protocol.http.json.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpJsonResponseEncoder extends MessageToMessageEncoder<HttpJsonResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonResponse msg, List<Object> out) throws Exception {
        ByteBuf body = Unpooled.copiedBuffer(JSONObject.toJSONString(msg.getResult()).getBytes());

        System.out.println("HttpJsonResponseEncoder :" + body.toString(CharsetUtil.UTF_8));

        FullHttpResponse response = msg.getHttpResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, body);
        } else {
            response = new DefaultFullHttpResponse(msg.getHttpResponse()
                    .protocolVersion(), msg.getHttpResponse().status(),
                    body);
        }
        response.headers().set(RtspHeaderNames.CONTENT_TYPE, "text/json");
        response.headers().set(RtspHeaderNames.CONTENT_LENGTH, body.readableBytes());

        out.add(response);
    }
}
