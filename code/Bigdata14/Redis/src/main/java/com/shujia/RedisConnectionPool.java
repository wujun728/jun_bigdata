package com.shujia;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConnectionPool {
    // Redis连接池
    public static void main(String[] args) {
        // 使用默认的配置创建Redis连接池
        JedisPool jedisPool = new JedisPool("master", 6379);

        // 从连接池中取出一个连接
        Jedis jedis = jedisPool.getResource();

        // 使用连接进行操作
        System.out.println(jedis.lrange("list1", 0, -1));

        // 关闭连接
        jedis.close();

        // 关闭连接池
        jedisPool.close();


    }
}
