/*
Date: 05/09,2019, 20:34
*/
package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import zookeeper.StatUtils;

public class BaseAPI {
    private static final String host = "120.78.193.198";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(host)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)) // 重连策略
                .authorization("digest", "fq:fq".getBytes())
//                .namespace("base")  //指定命名空间
                .build();
        client.start();

        String path = "/zk-curator/fq";
        String basepath = "/zk-curator";
        Stat stat = new Stat();

        // 增
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, "curator".getBytes());

        // 查
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("data: " + new String(bytes) + " stat: " + StatUtils.printStat(stat));

        // 改
        client.setData().withVersion(stat.getVersion()).forPath(path, "hello".getBytes());

        //删
        client.delete().forPath(path);
        client.delete().deletingChildrenIfNeeded().forPath(basepath); // 递归删除

        Thread.sleep(1000);
        client.close();
    }
}
