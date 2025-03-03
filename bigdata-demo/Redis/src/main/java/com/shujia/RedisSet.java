package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RedisSet {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }

    @Test
    // 创建Set
    public void SADD() {
        jedis.sadd("s1", "1", "2", "2", "2", "3", "4", "5", "6");
        jedis.sadd("s2", "5", "6", "7", "8", "9");
    }

    @Test
    // 移除元素
    public void SREM() {
        jedis.srem("s1", "1");
        jedis.srem("s1", "4");
    }

    @Test
    // 弹出一个元素，位置不确定
    public void SPOP() {
        String s1 = jedis.spop("s1");
        System.out.println(s1);
    }

    @Test
    // 获取所有的元素
    public void SMEMBERS() {
        Set<String> s1 = jedis.smembers("s1");
        for (String s : s1) {
            System.out.println(s);
        }
    }

    @Test
    // 集合常见的操作
    public void SETOP() {
        // 交集
        System.out.println(jedis.sinter("s1", "s2"));
        // 并集
        System.out.println(jedis.sunion("s1", "s2"));
        // 差集
        System.out.println(jedis.sdiff("s1", "s2"));

    }


    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }

}
