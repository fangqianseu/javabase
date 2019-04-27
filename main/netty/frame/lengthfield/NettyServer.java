/*
Date: 04/24,2019, 15:42

加入 长度字段 编码器
*/
package netty.frame.lengthfield;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer implements Runnable {
    private int port;

    public NettyServer(int port) throws InterruptedException {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new NettyServer(7777)).start();
    }

    @Override
    public void run() {
        // 配置线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 绑定相关信息
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 绑定线程池
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 绑定服务器读写操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加编码器 解决tcp半包问题
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2)); // 基于 头部加入 length字段
                            ch.pipeline().addLast(new LengthFieldPrepender(2));

                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NettyServerHandleAdapter());
                        }
                    });

            // 服务器异步创建绑定
            ChannelFuture future = bootstrap.bind(port).sync();
            // 关闭服务器通道
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
