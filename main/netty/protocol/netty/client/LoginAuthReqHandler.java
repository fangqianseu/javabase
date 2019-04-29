/*
Date: 04/29,2019, 20:41
*/
package netty.protocol.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.netty.struct.Header;
import netty.protocol.netty.struct.MessageType;
import netty.protocol.netty.struct.NettyMessage;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    /**
     * 建立连接时，产生 LOGIN_REQ 请求包,开始 boundout 事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    /**
     * 读取 LOGIN_RESP 事件
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.getvalue()) {
            String value = String.valueOf(message.getBody());

//            握手成功 传递给后一个channelhandle
            if (value.equals("0")) {
                System.out.println("Login is ok : " + message);
                ctx.fireChannelRead(msg);

//            握手失败 直接关闭
            } else {
                System.out.println("Login is error " + message);
                ctx.close();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();

        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getvalue());

        message.setHeader(header);

        return message;
    }
}
