/*
Date: 04/24,2019, 09:44
*/
package netty.commonnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean isStop;

    /**
     * 初始化 nio中的 selector 和 serverSocketChannel
     * 并绑定相关参数
     *
     * @param port
     */
    public NioServer(int port) {
        try {

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port), 1024);
        } catch (IOException e) {
            System.out.println(e.toString());
            System.exit(1);
        }
        this.isStop = false;
    }

    public static void main(String[] args) {
        new Thread(new NioServer(7777)).start();
    }

    public void stop() {
        isStop = true;
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                int readynum = selector.select(1000);
                if (readynum > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        try {
                            handleKey(key);

                        } catch (IOException e) {
                            e.printStackTrace();
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null)
                                    key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (selector != null)
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 新的连接请求
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);

                sc.register(selector, SelectionKey.OP_READ);
            }
            // 新的可读请求
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (sc.read(buffer) > 0) {
                    buffer.flip();

                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);

                    System.out.println(new String(bytes, "utf-8"));

                    doResponse(sc);
                }
                sc.close();
                key.cancel();
            }
        }
    }

    private void doResponse(SocketChannel sc) throws IOException {
        byte[] bytes = "your message is accepted".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if (!byteBuffer.hasRemaining())
            System.out.println("Send order 2 client succeed.");
    }
}
