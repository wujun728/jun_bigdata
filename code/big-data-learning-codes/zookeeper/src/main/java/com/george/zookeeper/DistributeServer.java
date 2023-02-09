package com.george.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * <p>
 *     监听服务器动态节点上下线
 *     服务端
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2021/1/6 21:50
 * @since JDK 1.8
 */
public class DistributeServer {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeServer server = new DistributeServer();
        // 1、连接集群
        server.getConnect();
        // 2、注册节点
        server.regist("host1");
        // 3、业务逻辑处理
        server.business();
    }

    /**
     * 业务处理
     * @throws InterruptedException
     */
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 注册节点
     * @param hostname
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void regist(String hostname) throws KeeperException, InterruptedException {
        zkClient.create("/servers/server", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println(hostname + "is online");
    }


    private String connectString = "hadoop02:2181,hadoop03:2181,hadoop04:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    /**
     * 连接集群
     * @throws IOException
     */
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });
    }
}
