
import redis.clients.jedis.Jedis; 
public class Demo_append {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Jedis jedis = new Jedis("10.20.14.125",6379);  //连接redis服务 
        //jedis.auth("abcdefg");  //密码验证-如果你没有设置redis密码可不验证即可使用相关命令 
          
        jedis.set("redis", "myredis");  //简单的key-value 存储  
        
        //在原有值得基础上添加,如若之前没有该key，则导入该key  
        //之前已经设定了redis对应"myredis",此句执行便会使redis对应"myredisyourredis"  
        jedis.append("redis", "yourredis");     
        jedis.append("content", "rabbit");         
        System.out.println(jedis.get("redis"));
        System.out.println(jedis.get("content"));
	}
}
