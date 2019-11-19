/*
Date: 05/09,2019, 10:36
*/
package zookeeper.base;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class Auth {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

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

        // 添加权限信息
        zooKeeper.addAuthInfo("digest", "fq:fq".getBytes());  // digest类似于 user:pw 类型
        String path = "/auth";
        zooKeeper.create(path, "auth".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);

        ZooKeeper zooKeeper2 = new ZooKeeper(host, 5000, null);
        zooKeeper.addAuthInfo("digest", "fq:wrong".getBytes()); //错误的权限信息
        try {
            byte[] data = zooKeeper2.getData(path, false, null); // 没有权限或权限失败 获取失败
        } catch (Exception e) {
            e.printStackTrace();
        }
        zooKeeper2.delete(path,-1);  // 不需权限也可删除该节点， 因删除权限作用于该节点的子节点，不包括本节点

        zooKeeper.close();
        zooKeeper2.close();
    }
}
