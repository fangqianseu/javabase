/*
Date: 04/24,2019, 16:18
*/
package netty.nettybasic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务器端的自定义读写操作
 */
public class NettyServerHandleAdapter extends ChannelInboundHandlerAdapter {

    /**
     * 读写都要通过 ByteBuf 来进行
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读操作
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        System.out.println(new String(req, "utf-8"));

        // 写操作
        ByteBuf res = Unpooled.copiedBuffer("hello client".getBytes());
        ctx.writeAndFlush(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}