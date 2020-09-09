
import redis.clients.jedis.Jedis; 

/**
 * �򵥸�ֵ
 */
public class Demo_string {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//        Jedis jedis = new Jedis("localhost",6379);  //����redis���� 
        RedisClient jedis = new RedisClient();  //����redis���� 
          
        //jedis.auth("abcdefg");  
          
        jedis.set("redis", "myredis");  
        System.out.println(jedis.get("redis"));
        long begin=System.currentTimeMillis();
        int i=0;
        while(i<10000){
        	i++;
        	jedis.set("redis1-test"+i, "fdsagdasg"+i);
        }
        long end=System.currentTimeMillis();
        System.err.println(end-begin);
	}
}
