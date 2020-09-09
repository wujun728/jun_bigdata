import java.util.List;
import java.util.Set;

//import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;  
  
//@Component("redisClient")  
public class RedisClient {  
  
     /** 
     * 通过key删除（字节） 
     * @param key 
     */  
    public void del(byte [] key){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.del(key);  
        RedisUtils.returnResource(jedis);  
    }  
    /** 
     * 通过key删除 
     * @param key 
     */  
    public void del(String key){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.del(key);  
        RedisUtils.returnResource(jedis);  
    }  
  
    /** 
     * 添加key value 并且设置存活时间(byte) 
     * @param key 
     * @param value 
     * @param liveTime 
     */  
    public void set(byte [] key,byte [] value,int liveTime){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.set(key, value);  
        jedis.expire(key, liveTime);  
        RedisUtils.returnResource(jedis);  
    }  
    /** 
     * 添加key value 并且设置存活时间 
     * @param key 
     * @param value 
     * @param liveTime 
     */  
    public void set(String key,String value,int liveTime){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.set(key, value);  
        jedis.expire(key, liveTime);  
        RedisUtils.returnResource(jedis);  
    }  
    /** 
     * 添加key value 
     * @param key 
     * @param value 
     */  
    public void set(String key,String value){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.set(key, value);  
        RedisUtils.returnResource(jedis);  
    }  
    /**添加key value (字节)(序列化) 
     * @param key 
     * @param value 
     */  
    public void set(byte [] key,byte [] value){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.set(key, value);  
        RedisUtils.returnResource(jedis);  
    }  
    /** 
     * 获取redis value (String) 
     * @param key 
     * @return 
     */  
    public String get(String key){  
        Jedis jedis = RedisUtils.getJedis();  
         String value = jedis.get(key);  
        RedisUtils.returnResource(jedis);  
        return value;  
    }  
    /** 
     * 获取redis value (byte [] )(反序列化) 
     * @param key 
     * @return 
     */  
    public byte[] get(byte [] key){  
        Jedis jedis = RedisUtils.getJedis();  
        byte[] value = jedis.get(key);  
        RedisUtils.returnResource(jedis);  
        return value;  
    }  
  
    /** 
     * 通过正则匹配keys 
     * @param pattern 
     * @return 
     */  
    public Set<String> keys(String pattern){  
        Jedis jedis = RedisUtils.getJedis();  
        Set<String> value = jedis.keys(pattern);  
        RedisUtils.returnResource(jedis);  
        return value;  
    }  
  
    /** 
     * 检查key是否已经存在 
     * @param key 
     * @return 
     */  
    public boolean exists(String key){  
        Jedis jedis = RedisUtils.getJedis();  
        boolean value = jedis.exists(key);  
        RedisUtils.returnResource(jedis);  
        return value;  
    }  
      
    /*******************redis list操作************************/  
    /** 
     * 往list中添加元素 
     * @param key 
     * @param value 
     */  
    public void lpush(String key,String value){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.lpush(key, value);  
        RedisUtils.returnResource(jedis);  
    }  
      
    public void rpush(String key,String value){  
        Jedis jedis = RedisUtils.getJedis();  
        jedis.rpush(key, value);  
        RedisUtils.returnResource(jedis);  
    }  
      
    /** 
     * 数组长度 
     * @param key 
     * @return 
     */  
    public Long llen(String key){  
        Jedis jedis = RedisUtils.getJedis();  
        Long len = jedis.llen(key);  
        RedisUtils.returnResource(jedis);  
        return len;  
    }  
      
    /** 
     * 获取下标为index的value 
     * @param key 
     * @param index 
     * @return 
     */  
    public String lindex(String key,Long index){  
        Jedis jedis = RedisUtils.getJedis();  
        String str = jedis.lindex(key, index);  
        RedisUtils.returnResource(jedis);  
        return str;  
    }  
      
    public String lpop(String key){  
        Jedis jedis = RedisUtils.getJedis();  
        String str = jedis.lpop(key);  
        RedisUtils.returnResource(jedis);  
        return str;  
    }  
      
    public List<String> lrange(String key,long start,long end){  
        Jedis jedis = RedisUtils.getJedis();  
        List<String> str = jedis.lrange(key, start, end);  
        RedisUtils.returnResource(jedis);  
        return str;  
    }  
    /*********************redis list操作结束**************************/  
      
    /** 
     * 清空redis 所有数据 
     * @return 
     */  
    public String flushDB(){  
        Jedis jedis = RedisUtils.getJedis();  
        String str = jedis.flushDB();  
        RedisUtils.returnResource(jedis);  
        return str;  
    }  
    /** 
     * 查看redis里有多少数据 
     */  
    public long dbSize(){  
        Jedis jedis = RedisUtils.getJedis();  
        long len = jedis.dbSize();  
        RedisUtils.returnResource(jedis);  
        return len;  
    }  
    /** 
     * 检查是否连接成功 
     * @return 
     */  
    public String ping(){  
        Jedis jedis = RedisUtils.getJedis();  
        String str = jedis.ping();  
        RedisUtils.returnResource(jedis);  
        return str;  
    }  
}  