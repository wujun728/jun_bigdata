import redis.clients.jedis.Jedis;


public class Demo_manyString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Jedis jedis = new Jedis("10.20.14.125",6379);  //连接redis服务
        
        //mset 是设置多个key-value值   参数（key1,value1,key2,value2,...,keyn,valuen）  
        //mget 是获取多个key所对应的value值  参数（key1,key2,key3,...,keyn）  返回的是个list  
        jedis.mset("name1","yangw","name2","demon","name3","elena");  
        System.out.println(jedis.mget("name1","name2","name3"));  
	}

}
