/*
Date: 04/29,2019, 16:37
*/
package netty.protocol.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.netty.struct.NettyMessage;

public class NettyServerHander extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        System.out.println("receive from client : " + message);
    }
}
