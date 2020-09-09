package com.ylzinfo.redis.model;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylzinfo.redis.model.subpub.Message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class Subscriber extends JedisPubSub {

    private static Logger logger = LoggerFactory.getLogger(Subscriber.class);
    private  Message messages = new Message();

    public  Message getMessage() {
    	
    	return messages;
    	
    	}
    @Override
    public void onMessage(String channel, String message) {
    	if(message.equals("quit")){
    		this.unsubscribe(channel);  
    		
    	}else{
    	PrintWriter out =null;	
    	try{	
    	HttpServletResponse res=messages.getResponse();
    	
    	out = res.getWriter();
    	out.write( message);
  
	/*	Thread.sleep(1000);
		this.unsubscribe(channel);  */
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		out.flush();
    		out.close();
    		this.unsubscribe(channel);
    	}
    	
        logger.info("Message received. Channel: {}, Msg: {}", channel, message);
    	}
    }
    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}