package com.shujia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisList {
    Jedis jedis;

    @Before
    // 会在执行@Test修饰的方法之前执行
    public void init() {
        jedis = new Jedis("master", 6379);
    }

    @Test
    // 创建一个List
    public void PUSH() {
        jedis.lpush("list1", "1");
        jedis.lpush("list1", "2");
        jedis.rpush("list1", "3");
        jedis.rpush("list1", "4");
        jedis.rpush("list1", "5");
    }

    @Test
    // 修改List中的元素
    public void LSET() {
        jedis.lset("list1", 4, "5.5");
    }

    @Test
    // 获取List中的所有元素
    public void LRANGE() {
        List<String> l = jedis.lrange("list1", 0, -1);
        for (String s : l) {
            System.out.println(s);
        }
    }


    @Test
    // 删除元素
    public void POP() {
        System.out.println(jedis.blpop(1000, "list1"));
        System.out.println(jedis.rpop("list1"));
        System.out.println(jedis.lpop("list1"));

    }


    @After
    // 表示在@Test方法执行完成之后执行
    public void closed() {
        jedis.close();
    }
}
