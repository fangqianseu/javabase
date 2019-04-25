/*
Date: 04/24,2019, 15:42

加入编码器 解决 tcp 粘包拆包问题
*/
package netty.nettytcpzhanbao.linebasedcodec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer implements Runnable {
    private int port;

    public NettyServer(int port) throws InterruptedException {
        this.port = port;
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
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); // 基于 分隔符 \r\n or \n 为结束标志
                            ch.pipeline().addLast(new StringDecoder());             // 将对象转化为字符串

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

    public static void main(String[] args) throws InterruptedException {
        new Thread(new NettyServer(7777)).start();
    }
}
