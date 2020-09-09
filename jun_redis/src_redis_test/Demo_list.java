
import redis.clients.jedis.Jedis;


public class Demo_list {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Jedis jedis = new Jedis("10.20.14.125",6379);  //连接redis服务  
        
        jedis.lpush("listDemo", "A");  
        jedis.lpush("listDemo", "B");  
        jedis.lpush("listDemo", "C");  
        System.out.println(jedis.lrange("listDemo", 0, -1));  
        System.out.println(jedis.lrange("listDemo", 0, 1)); 
        
        jedis.del("listDemo");  
        System.out.println(jedis.lrange("listDemo", 0, -1));  
	}

}
