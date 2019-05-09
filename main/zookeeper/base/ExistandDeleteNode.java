/*
Date: 05/09,2019, 10:10
*/
package zookeeper.base;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ExistandDeleteNode {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static CountDownLatch existSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Receive watched event：" + event);
                if (Event.KeeperState.SyncConnected == event.getState()) {
                    connectedSemaphore.countDown();
                }
            }
        });
        connectedSemaphore.await();
        String path = "/zk-exist";

        // 存在，返回node stat； 不存在，返回null
        Stat stat = zooKeeper.exists(path, true);
        if (stat == null) {
            System.out.println(path + " is not exist.");
            stat = new Stat();
        }
        zooKeeper.create(path, "exist".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);


        zooKeeper.exists(path, true, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                ((Stat) ctx).setVersion(stat.getVersion());
                existSemaphore.countDown();
            }
        }, stat);
        existSemaphore.await();
        zooKeeper.delete(path, stat.getVersion());

        zooKeeper.close();
    }
}
