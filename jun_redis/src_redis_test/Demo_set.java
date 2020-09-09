import redis.clients.jedis.Jedis;


public class Demo_set {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Jedis jedis = new Jedis("localhost",6379);  //����redis���� 
		
		jedis.sadd("sname", "wobby");  
        jedis.sadd("sname", "kings");  
        jedis.sadd("sname", "demon");  
        System.out.println(String.format("set num: %d", jedis.scard("sname")));  //����
        System.out.println(String.format("all members: %s", jedis.smembers("sname")));  //���е�ֵ
        System.out.println(String.format("is member: %B", jedis.sismember("sname", "wobby")));  //�ж��Ƿ����
        System.out.println(String.format("rand member: %s", jedis.srandmember("sname"))); //�����ȡ���� 
        
        jedis.srem("sname", "demon");  //ɾ��һ������  
        System.out.println(String.format("all members: %s", jedis.smembers("sname")));

	}

}
