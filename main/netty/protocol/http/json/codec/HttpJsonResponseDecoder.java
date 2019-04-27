/*
Date: 04/27,2019, 10:54
*/
package netty.protocol.http.json.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HttpJsonResponseDecoder extends MessageToMessageDecoder<FullHttpResponse> {
    private Class clazz;

    public HttpJsonResponseDecoder(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        if (msg.decoderResult().isFailure()) {
            System.out.println("HttpJsonResponse decode failed");
            return;
        }
        HttpJsonResponse httpJsonResponse = new HttpJsonResponse(msg, JSONObject.parseObject(msg.content().toString(CharsetUtil.UTF_8), clazz));
        System.out.println("Response Decoder :" + httpJsonResponse.getResult());
        out.add(httpJsonResponse);
    }
}
