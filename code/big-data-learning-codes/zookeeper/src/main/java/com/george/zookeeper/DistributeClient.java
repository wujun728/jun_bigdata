package com.george.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2021/1/6 22:00
 * @since JDK 1.8
 */
public class DistributeClient {
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        DistributeClient client = new DistributeClient();
        // 1、连接集群
        client.getConnect();
        // 2、注册监听
        client.getChildren();
        // 3、业务处理
        client.getBusiness();
    }

    /**
     * 业务处理
     */
    private void getBusiness() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 获取子节点，并监听
     */
    private void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/servers", true);
        // 存储服务器节点主机名称集合
        List<String> list = new ArrayList<String>();
        for (String child : children) {
            byte[] data = zkClient.getData("/servers/" + child, false, null);
            list.add(new String(data));
        }
        System.out.println(list);
    }

    private String connectString = "hadoop02:2181,hadoop03:2181,hadoop04:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    /**
     * 连接zookeeper集群
     */
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
