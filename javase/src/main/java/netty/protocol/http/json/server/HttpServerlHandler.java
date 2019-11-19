/*
Date: 04/27,2019, 10:05
*/
package netty.protocol.http.json.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.http.json.codec.HttpJsonRequest;
import netty.protocol.http.json.codec.HttpJsonResponse;
import netty.protocol.http.json.pojo.User;

public class HttpServerlHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpJsonRequest httpJsonRequest = (HttpJsonRequest) msg;

        User user = (User) httpJsonRequest.getBody();

        System.out.println("Http server receive from " + httpJsonRequest.getRequest().uri() + " request : " + user);

        User server = new User(1, "server", "fq@123");

        HttpJsonResponse httpJsonResponse = new HttpJsonResponse(null, server);

        ctx.writeAndFlush(httpJsonResponse);
    }
}
