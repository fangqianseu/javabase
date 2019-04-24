/*
Date: 04/24,2019, 10:19
*/
package netty.commonnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient implements Runnable {
    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public NioClient(String host, int port) {
        this.host = host;
        this.port = port;
        stop = false;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            selector = Selector.open();

            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doHello(socketChannel);
            } else
                socketChannel.register(selector, SelectionKey.OP_CONNECT);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new NioClient("localhost", 7777)).start();
    }

    @Override
    public void run() {
        while (!stop) {
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
                            //  注意 此处应该 出错时才关闭 key的channal
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
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doHello(sc);
                }
            }
            // 新的可读请求
            if (key.isReadable()) {

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (sc.read(buffer) > 0) {
                    buffer.flip();

                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);

                    System.out.println(new String(bytes, "utf-8"));
                    stop = true;
                }
                sc.close();
                key.cancel();
            }
        }
    }

    private void doHello(SocketChannel sc) throws IOException {
        byte[] bytes = "hello server ".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }
}
