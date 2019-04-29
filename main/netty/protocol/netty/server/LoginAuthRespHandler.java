/*
Date: 04/29,2019, 20:12

服务器的连接认证
*/
package netty.protocol.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.netty.struct.Header;
import netty.protocol.netty.struct.MessageType;
import netty.protocol.netty.struct.NettyMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Boolean> clientsChecks = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_REQ.getvalue()) {
            String remoteAddress = ctx.channel().remoteAddress().toString();
            NettyMessage nettyMessage = null;
            if (clientsChecks.containsKey(remoteAddress)) {
                nettyMessage = buildResponse("-1");
            } else {
                nettyMessage = buildResponse("0");
                clientsChecks.put(remoteAddress, true);

//                从该处返回channelhandles，
                ctx.writeAndFlush(nettyMessage);
                System.out.println(remoteAddress + " has login..., " + nettyMessage);
            }
        } else {
//          跳过该 channelhandle，前往下一个 channelhandle
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        String remoteAddress = ctx.channel().remoteAddress().toString();
        if (clientsChecks.containsKey(remoteAddress)) {
            clientsChecks.remove(remoteAddress);
        }
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(String result) {
        NettyMessage message = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.getvalue());

        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
