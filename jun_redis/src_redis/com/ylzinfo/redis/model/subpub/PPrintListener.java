package com.ylzinfo.redis.model.subpub;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.ylzinfo.redis.model.Constants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
public class PPrintListener extends JedisPubSub{

	private String clientId;
	private PSubHandler handler;
	
	public PPrintListener(String clientId,Jedis jedis){
		this.clientId = clientId;
		handler = new PSubHandler(jedis);
	}
	
	@Override
	public void onMessage(String channel, String message) {
		//此处我们可以取消订阅
		if(message.equalsIgnoreCase("quit")){
			this.unsubscribe(channel);
		}
		handler.handle(channel, message);
	}
	
	private void message(String channel,String message){
		String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		System.out.println("message receive:" + message + ",channel:" + channel + "..." + time);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		System.out.println("message receive:" + message + ",pattern channel:" + channel);
		
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		handler.subscribe(channel);
		System.out.println("subscribe:" + channel + ";total channels : " + subscribedChannels);
		
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		handler.unsubscribe(channel);
		System.out.println("unsubscribe:" + channel + ";total channels : " + subscribedChannels);
		
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		System.out.println("unsubscribe pattern:" + pattern + ";total channels : " + subscribedChannels);
		
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		System.out.println("subscribe pattern:" + pattern + ";total channels : " + subscribedChannels);		
	}
	
	@Override
	public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
        for(String channel : channels){
        	handler.unsubscribe(channel);
        }
    }
	
	class PSubHandler {

		private Jedis jedis;
		PSubHandler(Jedis jedis){
			this.jedis = jedis;
		}
		public void handle(String channel,String message){
			int index = message.indexOf("/");
			if(index < 0){
				return;
			}
			Long txid = Long.valueOf(message.substring(0,index));
			String key = clientId + "/" + channel;
			while(true){
					String lm = jedis.lindex(key, 0);//获取第一个消息
					if(lm == null){
						break;
					}
					int li = lm.indexOf("/");
					//如果消息不合法，删除并处理
					if(li < 0){
						String result = jedis.lpop(key);//删除当前message
						//为空
						if(result == null){
							break;
						}
						message(channel, lm);
						continue;
					}
					Long lxid = Long.valueOf(lm.substring(0,li));//获取消息的txid
					//直接消费txid之前的残留消息
					if(txid >= lxid){
						jedis.lpop(key);//删除当前message
						message(channel, lm);
						continue;
					}else{
						break;
					}
			}
		}
		
		public void subscribe(String channel){
			String key = clientId + "/" + channel;
			boolean exist = jedis.sismember(Constants.SUBSCRIBE_CENTER,key);
			if(!exist){
				jedis.sadd(Constants.SUBSCRIBE_CENTER, key);
			}
		}
		
		public void unsubscribe(String channel){
			String key = clientId + "/" + channel;
			jedis.srem(Constants.SUBSCRIBE_CENTER, key);//从“活跃订阅者”集合中删除
			jedis.del(key);//删除“订阅者消息队列”
		}
	}
}