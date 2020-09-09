package com.ylzinfo.redis.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Publisher {

    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    private final Jedis publisherJedis;

    private final String channel;

    public Publisher(Jedis publisherJedis, String channel) {
        this.publisherJedis = publisherJedis;
        this.channel = channel;
    }

    public void start() {
        logger.info("Type your message (quit for terminate)");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String line = reader.readLine();

                if (!"quit".equals(line)) {
                    publisherJedis.publish(channel, line);
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            logger.error("IO failure while reading input, e");
        }
    }
    public void publish(String messege) {
        logger.info("Type your message (quit for terminate)");
        try {
         
            

                if (!"quit".equals(messege)) {
                    publisherJedis.publish(channel, messege);
                } else {
                	
                	return;
                }
            

        } catch (Exception e) {
            logger.error("IO failure while reading input, e");
        }
    }
}