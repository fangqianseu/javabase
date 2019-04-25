/*
Date: 04/24,2019, 16:18
*/
package netty.nettycodec.fastjson;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务器端的自定义读写操作
 */
public class NettyServerHandleAdapter extends ChannelInboundHandlerAdapter {
    private int count = 0;

    /**
     * 加入编码器后 msg类型为 string
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读操作
        User context = (User) msg;
        System.out.println(String.format("context: %s, total count : %s", context, ++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}