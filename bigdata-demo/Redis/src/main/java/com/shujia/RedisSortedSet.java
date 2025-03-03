package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZParams;
import redis.clients.jedis.resps.Tuple;

import java.util.Set;

public class RedisSortedSet {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }

    @Test
    // 创建一个有序集合
    public void ZADD() {
        // zs1 表示水果一月份的销量
        jedis.zadd("zs1", 10, "西瓜");
        jedis.zadd("zs1", 10, "西瓜");
        jedis.zadd("zs1", 7, "香蕉");
        jedis.zadd("zs1", 7, "香蕉");
        jedis.zadd("zs1", 7, "香蕉");
        jedis.zadd("zs1", 5, "芒果");
        jedis.zadd("zs1", 5, "芒果");
        jedis.zadd("zs1", 8, "草莓");

        // zs2 表示水果二月份的销量
        jedis.zadd("zs2", 9, "哈密瓜");
        jedis.zadd("zs2", 6, "西瓜");
        jedis.zadd("zs2", 8, "香蕉");
        jedis.zadd("zs2", 3, "香蕉");
        jedis.zadd("zs2", 5, "香蕉");
        jedis.zadd("zs2", 6, "甘蔗");
        jedis.zadd("zs2", 7, "芒果");
        jedis.zadd("zs2", 8, "草莓");
    }

    @Test
    // 查看水果的累计销量
    public void TwoMonthSUM() {
        Set<Tuple> s = jedis.zunionWithScores(new ZParams().aggregate(ZParams.Aggregate.SUM), "zs1", "zs2");
        System.out.println(s);
    }


    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }
}
