package com.shujia;

import redis.clients.jedis.Jedis;

public class RedisDemo1 {
    /**
     * 通过Java代码操作Redis
     */
    public static void main(String[] args) {
        // 1、建立连接
        Jedis jedis = new Jedis("master", 6379);
        // 2、测试连通性
        System.out.println(jedis.ping());

        String nk3 = jedis.get("nk3");
        System.out.println(nk3);

        // 关闭Redis连接
        jedis.close();
    }
}
