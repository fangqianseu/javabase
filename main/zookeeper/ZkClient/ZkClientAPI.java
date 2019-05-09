/*
Date: 05/09,2019, 19:17
*/
package zookeeper.ZkClient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZkClientAPI {
    private static final String host = "120.78.193.198";
    private static ZkClient zkClient = new ZkClient(host, 5000);

    public static void main(String[] args) throws InterruptedException {
        String path = "/zk-zkclient";
        Stat stat = new Stat();
        zkClient.addAuthInfo("digest", "fq:fq".getBytes());

        boolean exists = zkClient.exists(path);
        System.out.println(exists);

        zkClient.createPersistent(path, "zkclient", ZooDefs.Ids.OPEN_ACL_UNSAFE);
        String data = zkClient.readData(path, stat);
        System.out.println(data);

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath + " data changed. now is " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + " data delete");
            }
        });
        zkClient.writeData(path, "hello", stat.getVersion());

        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
            }
        });
        zkClient.createPersistent(path + "/c1", "hello child");
        zkClient.readData(path + "/c1", stat);

        zkClient.delete(path + "/c1", stat.getVersion());
        zkClient.delete(path);

        Thread.sleep(1000);
        zkClient.close();
    }
}
