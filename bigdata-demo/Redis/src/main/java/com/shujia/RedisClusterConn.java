package com.shujia;

import redis.clients.jedis.ConnectionPool;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Map;

public class RedisClusterConn {
    public static void main(String[] args) {
        // 使用JedisCluster与集群进行通信建立连接
        JedisCluster cluster = new JedisCluster(new HostAndPort("master", 6381));

        cluster.set("cs1", "vv1");

        System.out.println(cluster.get("cs1"));

        cluster.hset("chs1", "f1", "v1");
        cluster.hset("chs1", "f2", "v1");
        cluster.hset("chs1", "f3", "v1");

        Map<String, String> map = cluster.hgetAll("chs1");
        for (Map.Entry<String, String> kv : map.entrySet()) {
            System.out.println(kv.getKey());
            System.out.println(kv.getValue());
        }

        cluster.close();

    }
}
