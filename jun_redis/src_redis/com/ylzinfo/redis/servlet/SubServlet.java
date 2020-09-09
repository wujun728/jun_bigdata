package com.ylzinfo.redis.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.ylzinfo.redis.manager.CacheManager;
import com.ylzinfo.redis.manager.CacheManager.CachePoolName;
import com.ylzinfo.redis.model.Subscriber;
import com.ylzinfo.redis.model.subpub.Message;

public class SubServlet extends HttpServlet {
	private static final long serialVersionUID = 4237883104561672800L;
	public static final String CHANNEL_NAME = "commonChannel";

	static Logger logger = Logger.getLogger(SubServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		final CacheManager cacheManager = CacheManager.getInstance();

		final Jedis subscriberJedis = cacheManager
				.getResource(CachePoolName.subpub);

		final Subscriber subscriber = new Subscriber();

		res.setCharacterEncoding("utf-8");
		Message mess = subscriber.getMessage();
		mess.setResponse(res);

		try {

			subscriberJedis.subscribe(subscriber, CHANNEL_NAME);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cacheManager.returnResource(CachePoolName.subpub, subscriberJedis);

		}
	}
}
