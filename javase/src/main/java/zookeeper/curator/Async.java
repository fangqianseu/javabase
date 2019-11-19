/*
Date: 05/09,2019, 21:18
*/
package zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Async {
    private static final String host = "120.78.193.198";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(host)
                .retryPolicy(new RetryForever(3))
                .sessionTimeoutMs(5000)
                .build();
        client.start();


        String path = "/zk-async-creat";

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("event code: " + event.getResultCode() + " type: " + event.getType() + " data: " + event.getData());
                countDownLatch.countDown();
            }
        }, executorService).forPath(path, "init".getBytes());

        countDownLatch.await();
        executorService.shutdown();
        client.close();
    }
}
