/*
Date: 04/29,2019, 16:35
*/
package netty.protocol.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.netty.struct.Header;
import netty.protocol.netty.struct.MessageType;
import netty.protocol.netty.struct.NettyMessage;

import java.util.Date;
import java.util.HashMap;

public class NettyClientHander extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage nettyMessage = new NettyMessage();

        Header header = new Header();
        header.setLength(12);
        header.setType(MessageType.ONE_WAY.getvalue());
        header.setSessionID(1231212l);
        header.setPriority((byte) 2);
        HashMap<String, Object> map = new HashMap<>();
        map.put("fq", new Date());
        header.setAttachment(map);

        nettyMessage.setHeader(header);
        nettyMessage.setBody(map);

        ctx.writeAndFlush(nettyMessage);
        System.out.println("send :" + nettyMessage);
    }
}
