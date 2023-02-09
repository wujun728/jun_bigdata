package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.BitOP;

public class RedisString {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }


    @Test
    // 增加一个String类型的value
    public void Set() {
        jedis.set("j1", "v1");
        jedis.set("j2", "v2");
        jedis.set("j3", "v3");

    }

    @Test
    // 删除一个K-V
    public void DEL() {
        jedis.del("j1");
    }

    @Test
    // 根据K获取V
    public void GET() {
        System.out.println(jedis.get("j1"));
        System.out.println(jedis.get("j2"));
        System.out.println(jedis.get("j3"));
    }

    @Test
    // 创建一个位图
    public void SETBIT() {
        jedis.setbit("b1", 1, true);
        jedis.setbit("b2", 3, true);
    }

    @Test
    // 获取位图
    public void GETBIT() {
        System.out.println(jedis.get("b1"));
    }

    @Test
    // 位图的操作
    public void BITOPT() {
        jedis.bitop(BitOP.AND, "b3", "b1", "b2");
        jedis.bitop(BitOP.OR, "b4", "b1", "b2");
        jedis.bitop(BitOP.NOT, "b5", "b1");
        jedis.bitop(BitOP.XOR, "b6", "b1", "b2");
        System.out.println(jedis.get("b3"));
        System.out.println(jedis.get("b4"));
        System.out.println(jedis.get("b5"));
        System.out.println(jedis.get("b6"));
    }


    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }
}
