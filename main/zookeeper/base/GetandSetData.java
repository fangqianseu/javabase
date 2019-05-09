/*
Date: 05/09,2019, 08:32
*/
package zookeeper.base;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetandSetData {
    private static final String host = "120.78.193.198";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static CountDownLatch getSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
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

        String path = "/zk-test-Sync";
        Stat stat = new Stat();

        // 同步方式
        // 为 true， 则注册连接 zookeeper 时使用的 watcher 监听该节点的数据变化
        byte[] data = zooKeeper.getData(path, true, stat);
        System.out.println("Get data [" + new String(data) + "] from path [" + path + "] , " + StatUtils.printStat(stat));
        // 更改data 同步 触发注册的 watcher
        stat = zooKeeper.setData(path, "hello".getBytes(), -1);
        // 使用 exists, 重新注册 watcher
        stat = zooKeeper.exists(path, true);


        // 异步方式
        // 建立子节点
        String childpath = "/child";
        zooKeeper.create(path + childpath, "is child".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 注册得到数据的回调函数
        zooKeeper.getChildren(path, true, new AsyncCallback.Children2Callback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                System.out.println("Get Children znode result: [ " + rc + ", path: " + path
                        + ", children num: " + children.size() + ", stat: " + StatUtils.printStat(stat));
                // children 中 为 子节点路径
                if (!children.isEmpty()) {
                    for (String cpath : children) {
                        byte[] d = null;
                        try {
                            d = zooKeeper.getData(path + "/" + cpath, true, (Stat) ctx);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("child path : " + cpath + ", data: " + new String(d));
                    }
                }
                getSemaphore.countDown();
            }
        }, stat);
        getSemaphore.await();

        // 更改 data 异步 注册监听函数
        zooKeeper.setData(path + childpath, "set data".getBytes(), stat.getVersion(), new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                if (rc == 0)
                    System.out.println("Async change " + path + " data success");
                else
                    System.out.println("Async change " + path + " data failed, code :" + rc);
            }
        }, null);
        Thread.sleep(1000);

        zooKeeper.close();
    }
}
