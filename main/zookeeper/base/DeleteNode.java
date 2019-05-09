/*
Date: 05/09,2019, 08:20
*/
package zookeeper.base;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DeleteNode {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static CountDownLatch deleteSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Receive watched event：" + event);
                if (Event.KeeperState.SyncConnected == event.getState())
                    connectedSemaphore.countDown();
            }
        });
        connectedSemaphore.await();

        String path = "/zk-test-delete";
        String res = zooKeeper.create(path, "Sync".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Node create: " + res);

        // 同步删除
        // -1 代表匹配任意 version
        zooKeeper.delete(path, -1);
        System.out.println("Sync delete");

        // 异步删除
        zooKeeper.delete(path, -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println("Async delete: rc = " + rc + " path: " + path);
                deleteSemaphore.countDown();
            }
        }, "");
        deleteSemaphore.await();
        zooKeeper.close();
    }
}
