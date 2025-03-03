package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisHash {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }

    @Test
    // 创建一个Hash散列
    public void HSET() {
        jedis.hset("hash1", "id", "1");
        jedis.hset("hash1", "name", "张三");
        jedis.hset("hash1", "age", "18");
        jedis.hset("hash1", "gender", "男");
        jedis.hset("hash1", "clazz", "文科四班");
    }

    @Test
    // 获取Hash所有的key
    public void HKEYS() {
        Set<String> s = jedis.hkeys("hash1");
        for (String s1 : s) {
            System.out.println(s1);
        }
    }

    @Test
    // 获取Hash所有的Value
    public void HVALS() {
        List<String> l = jedis.hvals("hash1");
        for (String s : l) {
            System.out.println(s);
        }
    }

    @Test
    // 获取Hash所有的K-V
    public void HGETALL() {
        Map<String, String> m = jedis.hgetAll("hash1");
        for (Map.Entry<String, String> kv : m.entrySet()) {
            System.out.println(kv.getKey());
            System.out.println(kv.getValue());
        }
    }

    @Test
    // 指定Field获取Value
    public void HGET() {
        System.out.println(jedis.hget("hash1", "name"));
    }

    @Test
    // 根据Field删除Value
    public void HDEL() {
        jedis.hdel("hash1", "gender");
    }

    @Test
    // 删除整个Hash散列
    public void DEL() {
        jedis.del("hash1");
    }

    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }
}
