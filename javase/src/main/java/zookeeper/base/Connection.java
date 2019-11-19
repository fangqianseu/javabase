/*
Date: 05/08,2019, 21:18

zookeeper连接操作
*/
package zookeeper.base;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class Connection {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        // 异步连接 需要等待
        ZooKeeper zookeeper = new ZooKeeper(host, 5000, new SimpleWatcher());
        System.out.println(zookeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
        }
        System.out.println("ZooKeeper session first established.");

        // 使用上一次连接的凭证
        long sessionId = zookeeper.getSessionId();
        byte[] passwd = zookeeper.getSessionPasswd();
        System.out.println("sessionID: " + sessionId + "， passed:" + new String(passwd));

        zookeeper = new ZooKeeper(host, 5000, new SimpleWatcher(), sessionId, passwd);

        System.out.println("ZooKeeper session second established.");
        Thread.sleep(1000);
        zookeeper.close();
    }

    static class SimpleWatcher implements Watcher {
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event：" + event);
            if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
