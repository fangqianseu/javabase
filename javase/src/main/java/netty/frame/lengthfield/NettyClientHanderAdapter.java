/*
Date: 04/24,2019, 16:28
*/
package netty.frame.lengthfield;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHanderAdapter extends ChannelInboundHandlerAdapter {
    /**
     * 标准写操作 通过 bytebuf
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            ByteBuf req = Unpooled.copiedBuffer("Hello server".getBytes());
            ctx.writeAndFlush(req);
        }
    }

    /**
     * 加入编码器后 msg 直接为 string 类型
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String context = (String) msg;
        System.out.println(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
