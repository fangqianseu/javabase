/*
Date: 05/08,2019, 21:32
*/
package zookeeper.base;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.Watcher.Event.KeeperState;

public class CreateNode {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static CountDownLatch createNodeSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zookeeper = new ZooKeeper(host, 5000, new SimpleWatcher());
        connectedSemaphore.await();

        // 同步方式创建节点
        System.out.println("Create Sync");
        String res = zookeeper.create("/zk-test-Sync", "Sync".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Success create znode: " + res);

        // 异步方式 创建节点
        System.out.println("Create ASync");
        zookeeper.create("/zk-test-ASync", "ASync".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new AsyncCallback.StringCallback() {
                    /**
                     * 创建节点的异步函数
                     * @param rc 服务端相应码
                     * @param path
                     * @param ctx create的最后一个传入参数 本例为 new ArrayList<>()
                     * @param name
                     */
                    @Override
                    public void processResult(int rc, String path, Object ctx, String name) {
                        System.out.println("Create path result: [" + rc + ", " + path + ", "
                                + ctx + ", real path name: " + name);
                        createNodeSemaphore.countDown();
                    }
                }, new ArrayList<>());
        createNodeSemaphore.await();

        zookeeper.close();
    }

    static class SimpleWatcher implements Watcher {
        public void process(WatchedEvent event) {
            if (KeeperState.SyncConnected == event.getState()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
