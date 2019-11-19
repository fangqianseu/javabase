/*
Date: 04/27,2019, 10:02
*/
package netty.protocol.http.json.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import netty.protocol.http.json.codec.HttpJsonRequestDecoder;
import netty.protocol.http.json.codec.HttpJsonResponseEncoder;
import netty.protocol.http.json.pojo.User;

public class HttpJsonServer {
    private int port;

    public HttpJsonServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        HttpJsonServer httpFileServer = new HttpJsonServer(8888);
        httpFileServer.start();
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new HttpRequestDecoder());
//                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));


                            ch.pipeline().addLast(new HttpJsonRequestDecoder(User.class));
                            ch.pipeline().addLast(new HttpJsonResponseEncoder());

                            ch.pipeline().addLast(new HttpServerlHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("HTTP文件系统已启动。。。。http://223.3.79.173:" + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
