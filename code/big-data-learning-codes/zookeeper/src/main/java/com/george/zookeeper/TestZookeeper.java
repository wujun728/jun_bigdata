package com.george.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2021/1/6 20:41
 * @since JDK 1.8
 */
public class TestZookeeper {
    private String connectString = "hadoop02:2181,hadoop03:2181,hadoop04:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    /**
     * 创建客户端连接
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                // 收到事件通知后的回调函数（用户的业务逻辑）
                System.out.println(event.getType() + "--" + event.getPath());
                // 再次启动监听
                try {
                    zkClient.getChildren("/", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void create() throws KeeperException, InterruptedException {
        // 参数1：要创建的节点的路径； 参数2：节点数据 ； 参数3：节点权限 ；参数4：节点的类型
        zkClient.create("/test", "test data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取子节点并监听节点变化
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void exists() throws KeeperException, InterruptedException {
        Stat exists = zkClient.exists("/test", false);
        // 节点不存在则结果为null
        System.out.println(exists);
    }
}
