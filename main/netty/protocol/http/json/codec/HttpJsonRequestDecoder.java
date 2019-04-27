/*
Date: 04/27,2019, 09:39
*/
package netty.protocol.http.json.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HttpJsonRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    private Class clazz;

    public HttpJsonRequestDecoder(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (msg.decoderResult().isFailure()) {
            System.out.println("HttpJsonRequest decode failed");
            return;
        }
        HttpJsonRequest httpJsonRequest = new HttpJsonRequest(msg, JSONObject.parseObject(msg.content().toString(CharsetUtil.UTF_8), clazz));

        System.out.println("HttpJsonRequestDecoder:" + httpJsonRequest.getBody().toString());

        out.add(httpJsonRequest);
    }
}
