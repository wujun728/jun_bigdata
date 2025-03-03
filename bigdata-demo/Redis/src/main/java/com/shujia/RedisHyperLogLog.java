package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisHyperLogLog {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }

    @Test
    public void PFADD() {
        jedis.pfadd("hll1", "1", "1", "2", "3", "4", "4", "5");
        jedis.pfadd("hll2", "1", "3", "4", "7", "4", "8", "5");
    }

    @Test
    // 求一组数据（可能重复）的基数
    public void PFCOUNT() {
        System.out.println(jedis.pfcount("hll1"));
        System.out.println(jedis.pfcount("hll2"));
    }

    @Test
    // 合并两个HyperLogLog
    public void PFMERGE() {
        jedis.pfmerge("hll3", "hll1", "hll2");
        System.out.println(jedis.pfcount("hll3"));
    }


    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }
}
