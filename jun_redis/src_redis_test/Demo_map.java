import redis.clients.jedis.Jedis;
import java.util.Map; 
import java.util.HashMap;  
import java.util.List; 

public class Demo_map {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Jedis jedis = new Jedis("10.20.14.125",6379);  //连接redis服务  
        
        Map<String,String> user = new HashMap<String,String>();  
        user.put("name", "cd");  
        user.put("password", "123456");  
        
        jedis.hmset("user", user);  //map存入redis
        System.out.println(String.format("len:%d", jedis.hlen("user")));  //mapkey个数 
        System.out.println(String.format("keys: %s", jedis.hkeys("user") ));  //map中的所有键值
        System.out.println(String.format("values: %s", jedis.hvals("user") ));  //map中的所有value  
        
        List<String> rsmap = jedis.hmget("user", "name","password");  //取出map中的name、password字段值
        System.out.println(rsmap);  
        
        jedis.hdel("user", "password");  //删除map中的某一个键值 password  
        System.out.println(jedis.hmget("user", "name", "password"));  
	}

}
