/*
Date: 04/27,2019, 19:16
*/
package netty.protocol.http.json.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import netty.protocol.http.json.codec.HttpJsonRequestEncoder;
import netty.protocol.http.json.codec.HttpJsonResponseDecoder;
import netty.protocol.http.json.pojo.User;

public class HttpJsonClient {
    private int port;
    private String ip;

    public HttpJsonClient(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public static void main(String[] args) {
        new HttpJsonClient("127.0.0.1", 8888).start();
    }

    public void start() {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new HttpRequestEncoder());
//                            ch.pipeline().addLast(new HttpResponseDecoder());
                            ch.pipeline().addLast(new HttpClientCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));

                            ch.pipeline().addLast(new HttpJsonRequestEncoder());
                            ch.pipeline().addLast(new HttpJsonResponseDecoder(User.class));

                            ch.pipeline().addLast(new HttpClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
