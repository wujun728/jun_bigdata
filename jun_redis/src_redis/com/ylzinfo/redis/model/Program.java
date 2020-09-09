package com.ylzinfo.redis.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylzinfo.redis.manager.CacheManager;
import com.ylzinfo.redis.manager.CacheManager.CachePoolName;

import redis.clients.jedis.Jedis;

public class Program {

    public static final String CHANNEL_NAME = "commonChannel";

    private static Logger logger = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) throws Exception {
    	final CacheManager cacheManager = CacheManager.getInstance();
        final Jedis subscriberJedis =  cacheManager.getResource(CachePoolName.upload);	
      
        final Subscriber subscriber = new Subscriber();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
                    subscriberJedis.subscribe(subscriber, CHANNEL_NAME);
                    logger.info("Subscription ended.");
                } catch (Exception e) {
                    logger.error("Subscribing failed.", e);
                }
            }
        }).start();

        Jedis publisherJedis = cacheManager.getResource(CachePoolName.upload);	

        new Publisher(publisherJedis, CHANNEL_NAME).start();
     
    	subscriber.unsubscribe(CHANNEL_NAME);

    	//cacheManager.returnResource(CachePoolName.upload, subscriberJedis);
    	cacheManager.returnResource(CachePoolName.upload, publisherJedis);

    }
}