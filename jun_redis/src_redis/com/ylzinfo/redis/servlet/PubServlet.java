package com.ylzinfo.redis.servlet;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.ylzinfo.redis.manager.CacheManager;
import com.ylzinfo.redis.manager.CacheManager.CachePoolName;
import com.ylzinfo.redis.model.Publisher;
import com.ylzinfo.redis.model.Subscriber;

public class PubServlet extends HttpServlet {
	private static final long serialVersionUID = 426788804561672800L;
	public static final String CHANNEL_NAME = "commonChannel";
	static Logger logger = Logger.getLogger(SubServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, UnsupportedEncodingException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, UnsupportedEncodingException {

		final CacheManager cacheManager = CacheManager.getInstance();
		String content = req.getParameter("content");
		Jedis publisherJedis = cacheManager.getResource(CachePoolName.upload);

		new Publisher(publisherJedis, CHANNEL_NAME).publish(content);

		
		cacheManager.returnResource(CachePoolName.upload, publisherJedis);

	}
}
