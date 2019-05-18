/*
Date: 05/18,2019, 18:47
*/
package netty.longconnection;

import io.netty.channel.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientChannelAdapter extends SimpleChannelInboundHandler<String> {
    public AtomicInteger atomicInteger = new AtomicInteger(0);
    private volatile Channel channel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        atomicInteger.incrementAndGet();
        System.out.println(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connect to server + " + ctx.channel().remoteAddress());
        channel = ctx.channel();
    }

    public void send(String msg) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("send complete");
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    public void close() {
        channel.close();
    }
}
