/*
Date: 04/24,2019, 16:28
*/
package netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class NettyClientHanderAdapter extends ChannelInboundHandlerAdapter {
    /**
     * 标准写操作 通过 bytebuf
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf req = Unpooled.copiedBuffer( "Hello server".getBytes());
        ctx.writeAndFlush(req);
    }

    /**
     * 标准读操作 通过 bytebuf
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        ByteBuf req = buf.readBytes(buf.readableBytes());

        System.out.println(req.toString(Charset.forName("utf-8")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
