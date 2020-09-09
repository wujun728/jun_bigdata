package com.ylzinfo.redis.model.subpub;

import java.util.Set;

import com.ylzinfo.redis.manager.CacheManager;
import com.ylzinfo.redis.manager.CacheManager.CachePoolName;
import com.ylzinfo.redis.model.Constants;

import redis.clients.jedis.Jedis;


public class PPubClient {

	private Jedis jedis;//
	public PPubClient(String host,int port){
		jedis = new Jedis(host,port);
	}
	
	/**
	 * 发布的每条消息，都需要在“订阅者消息队列”中持久
	 * @param message
	 */
	private void put(String message){
		//期望这个集合不要太大
		Set<String> subClients = jedis.smembers(Constants.SUBSCRIBE_CENTER);
		for(String clientKey : subClients){
			jedis.rpush(clientKey, message);
		}
	}
	
	public void pub(String channel,String message){
		//每个消息，都有具有一个全局唯一的id
		//txid为了防止订阅端在数据处理时“乱序”，这就要求订阅者需要解析message
		Long txid = jedis.incr(Constants.MESSAGE_TXID);
		String content = txid + "/" + message;
		//非事务
		this.put(content);
		jedis.publish(channel, content);//为每个消息设定id，最终消息格式1000/messageContent
		
	}
	
	public void close(String channel){
		jedis.publish(channel, "quit");
		jedis.del(channel);//删除
	}
	
	public void test(){
		jedis.set("pub-block", "15");
		String tmp = jedis.get("pub-block");
		System.out.println("TEST:" + tmp);
	}


}