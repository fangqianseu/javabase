/*
Date: 04/27,2019, 19:19
*/
package netty.protocol.http.json.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.http.json.codec.HttpJsonRequest;
import netty.protocol.http.json.codec.HttpJsonResponse;
import netty.protocol.http.json.pojo.User;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect to server");

        User user = new User(2, "client", "fq124");
        HttpJsonRequest httpJsonRequest = new HttpJsonRequest(null, user);

        ctx.writeAndFlush(httpJsonRequest);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpJsonResponse response = (HttpJsonResponse) msg;
        System.out.println("resever from server: " + response.getResult());
    }
}
