package com.ylzinfo.redis.storage;



import com.ylzinfo.redis.config.SysConstants;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
* @Description:缓存池
* @author Wujun
* @date 2013-7-21 
*
 */
public class CachePool {
	
	private JedisPool pool = null;
	
	private JedisPoolConfig poolConfig = null;
	
	private String host; // ip
	private int port; // 端口
	private int db; // select 第几个数据库
	public CachePool(String host, Integer port, Integer db)
	{
		this.host = host;
		this.port = port;
		this.db = db;
		
	}
	
	/**
	 * @Description: 初始配置
	 */
	public void initialConfig()
	{
		if(poolConfig == null)
		{
			poolConfig = new JedisPoolConfig(); 
			poolConfig.setTestOnBorrow(SysConstants.TEST_ON_BORROW); 
			poolConfig.setMaxTotal(SysConstants.MAX_ACTIVE);
			poolConfig.setMaxIdle(SysConstants.MAX_IDLE); 
			poolConfig.setMinIdle(SysConstants.MIN_IDLE);
			poolConfig.setMaxWaitMillis(SysConstants.MAX_WAIT);  
			poolConfig.setTestWhileIdle(SysConstants.TEST_WHILE_IDLE); 
		}
	}
	
	/**
	 * 
	 * @Description: 加载
	 */
	public void launch()
	{
		if(pool == null)
		{
			initialConfig();
			try{
			pool = new JedisPool(poolConfig, this.host, this.port,0,"redis");
			
			}catch (Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 * @Description: 销毁
	 */
	public void destory()
	{
		if(pool != null)
		{
			pool.destroy();
		}
	}
	
	/**
	 * 
	 * @Description: 
	 * @return redis资源连接
	 */
	public Jedis getResource()
	{
		Jedis jedisInstance = null;
		if(pool != null)
		{
			jedisInstance = pool.getResource();
			if(db > 0)
			{
				jedisInstance.select(db);
			}
		}
		return jedisInstance;
	}
	
	/**
	 * 
	 * @Description: 获取redis连接资源
	 * @param jedisInstance
	 */
	public void returnResource(Jedis jedisInstance)
	{
		pool.returnResource(jedisInstance);
	}

}
