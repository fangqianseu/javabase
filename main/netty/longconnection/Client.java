/*
Date: 05/18,2019, 18:28
*/
package netty.longconnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Client {
    private volatile ClientChannelAdapter socketChannel;
    private volatile NioEventLoopGroup group;

    public static void main(String[] args) throws InterruptedException, IOException {
        Client client = new Client();
        client.connect("localhost", 8000);

        for (int i = 0; i < 10; i++) {
            client.send("alsdj");
        }

        Thread.sleep(1000);  // 等待server返回
        client.close();
    }

    public void connect(String host, int port) {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加编码器 解决tcp半包问题
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast(new LengthFieldPrepender(2))

                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())

                                    .addLast(new ClientChannelAdapter());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            CountDownLatch countDownLatch = new CountDownLatch(1);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("Connect to success");
                        socketChannel = (ClientChannelAdapter) future.channel().pipeline().get(ClientChannelAdapter.class);
                    } else {
                        System.out.println("connect failed");
                        socketChannel = null;
                    }
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) throws InterruptedException {
        if (socketChannel != null)
            socketChannel.send(msg);
    }

    /**
     * 关闭 client的线程池
     */
    public void close() {
        System.out.println(socketChannel.atomicInteger.get());
        socketChannel.close();
        group.shutdownGracefully();
    }
}
