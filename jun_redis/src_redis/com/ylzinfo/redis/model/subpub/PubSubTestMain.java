package com.ylzinfo.redis.model.subpub;

import org.apache.commons.lang.RandomStringUtils;

import redis.clients.jedis.JedisPubSub;

import com.ylzinfo.redis.model.Constants;

public class PubSubTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		PPubClient pubClient = new PPubClient(Constants.host, Constants.port);
		final String channel = "pubsub-channel-p";
		final PSubClient subClient = new PSubClient(Constants.host, Constants.port,"subClient-1");
		Thread subThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("----------subscribe operation begin-------");
				//在API级别，此处为轮询操作，直到unsubscribe调用，才会返回
				subClient.sub(channel);
				System.out.println("----------subscribe operation end-------");
				
			}
		});
		subThread.setDaemon(true);
		subThread.start();
		int i = 0;
		while(i < 9){
			String message = RandomStringUtils.random(6, true, true);//apache-commons
			pubClient.pub(channel, message);
			i++;
			Thread.sleep(1000);
		}
		subClient.unsubscribe(channel);
	}
	
}