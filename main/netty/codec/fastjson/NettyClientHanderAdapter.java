/*
Date: 04/24,2019, 16:28
*/
package netty.codec.fastjson;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

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
            User user = new User();
            user.setId(i);
            user.setName("fq" + i);
            user.setAge(i + "");
            user.setDate(new Date());

            ctx.writeAndFlush(user);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
