/*
Date: 04/24,2019, 16:17

加入编码器 解决 tcp 粘包拆包问题
*/
package netty.nettyframe.linebased;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {

    public void connect(String host, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加编码器 解决tcp半包问题
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); // 基于 分隔符 \r\n or \n 为结束标志
                            ch.pipeline().addLast(new StringDecoder());             // 将对象转化为字符串

                            ch.pipeline().addLast(new NettyClientHanderAdapter());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args){
        new NettyClient().connect("localhost",7777);
    }
}
