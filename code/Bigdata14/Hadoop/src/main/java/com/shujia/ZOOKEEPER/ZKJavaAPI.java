package com.shujia.ZOOKEEPER;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ZKJavaAPI {
    ZooKeeper zk;

    @Before
    public void init() throws IOException {
        zk = new ZooKeeper("master:2181,node1:2181,node2:2181", 100000, null);

    }

    @Test
    public void createPersistentZNODE() throws InterruptedException, KeeperException {
        /**
         * ZooDefs.Ids :控制所创建的ZNODE的权限
         * OPEN_ACL_UNSAFE  : 完全开放的ACL，任何连接的客户端都可以操作该属性znode
         * CREATOR_ALL_ACL : 只有创建者才有ACL权限
         * READ_ACL_UNSAFE：只能读取ACL
         *
         * CreateMode : 创建的ZNODE的类型
         * PERSISTENT : 永久创建，连接断开不会删除
         * EPHEMERAL : 创建临时节点，连接断开即删除
         */
        zk.create("/test1"
                , "abcdefg".getBytes()
                , ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.PERSISTENT);
    }

    @Test
    // 创建临时节点
    public void createEPHEMERALZNODE() throws InterruptedException, KeeperException {
        zk.create("/test3"
                , "efg".getBytes()
                , ZooDefs.Ids.OPEN_ACL_UNSAFE
                , CreateMode.EPHEMERAL);
    }

    @Test
    // 获取节点的数据
    public void getZNODE() throws InterruptedException, KeeperException {
        byte[] data = zk.getData("/test1", null, new Stat());
        System.out.println(new String(data));
    }

    @Test
    // 修改数据
    public void setZNODE() throws InterruptedException, KeeperException {
        zk.setData("/test", "abcd".getBytes(), 0);
    }

    @Test
    // 删除节点
    public void deleteZNode() throws InterruptedException, KeeperException {
        zk.delete("/test1", 0);
    }

    @Test
    // 事件
    public void triggerWatcher() throws InterruptedException, KeeperException {
        zk.exists("/test", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("/test 发生了变化");
                System.out.println(event.getPath());
                System.out.println(event.getState());
                System.out.println(event.getType());
                System.out.println(event.getWrapper());
            }
        });

        while (true) {

        }
    }


    @After
    public void closed() throws InterruptedException {
        zk.close();
    }
}
