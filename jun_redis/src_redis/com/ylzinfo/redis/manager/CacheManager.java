package com.ylzinfo.redis.manager;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;



import com.ylzinfo.redis.storage.CachePool;

import redis.clients.jedis.Jedis;


public class CacheManager
{
	private ConcurrentHashMap<String, CachePool> cachePool = new ConcurrentHashMap<String, CachePool>(); 
	
	private static CacheManager cacheManagerInstance = new CacheManager();
	
	private Properties cacheProperties;
	
	private boolean isLanuch = false;
	/*
	 * 创建一个单例
	 */
	public static CacheManager getInstance()
	{
		if(cacheManagerInstance == null)
		{
			cacheManagerInstance = new CacheManager();
		}
		return cacheManagerInstance;
	}
	/*
	 * 根据配置创建实例
	 */
	private CacheManager()
	{
		this.cacheProperties = ConfigureManager.getInstance().getCacheConfig();
		launch();
	}
	/*
	 * 加载数据池
	 */

	private void launch()
	{
		if( ! isLanuch)
		{
			
			String needLaunchPoolInstance = cacheProperties.getProperty("instances");
			String[] needLaunchPools = needLaunchPoolInstance.split(",");
			for(String instance : needLaunchPools)
			{
			
				String redisHost = cacheProperties.getProperty(instance + "_host");
				Integer redisPort = Integer.valueOf(cacheProperties.getProperty(instance + "_port"));
				/*
				 * 获取数据库，一个redis默认可以配置16个数据库
				 */
				Integer redisDB = Integer.valueOf(cacheProperties.getProperty(instance + "_db"));
				/*
				 * 加入池
				 */
				CachePool cachePoolInstance = new CachePool(redisHost, redisPort, redisDB);
				cachePoolInstance.launch();
				cachePool.put(instance, cachePoolInstance);
			}
			isLanuch = true;
		}
	}
	
	
	public void shutDown()
	{
		if(this.cachePool.size() > 0)
		{
			Iterator<Entry<String, CachePool>> iterator = this.cachePool.entrySet().iterator();
			while(iterator.hasNext())
			{
				Map.Entry<String, CachePool> hashEntry = iterator.next();
				CachePool poolInstance = hashEntry.getValue();
				poolInstance.destory();
				
				cachePool.remove(hashEntry.getKey());
			}
			
			cachePool.clear();
		}
	}
	
	
	public Jedis getResource(CachePoolName poolName) {
		String strName = poolName.toString();
		
		return getJedisResource(strName);
	}
	

	public void returnResource(CachePoolName poolName,  Jedis jedis) {
		if(jedis == null) {
			return ;
		}
		
		String strName = poolName.toString();
		returnJedisResource(strName, jedis);
	}
	public void returnResourcetofinal(CachePoolName poolName, final Jedis jedis) {
		if(jedis == null) {
			return ;
		}
		
		String strName = poolName.toString();
		returnJedisResourcetofinal(strName, jedis);
	}

	private void returnJedisResource(String poolName, Jedis jedisInstance) {
		CachePool pool = cachePool.get(poolName);
		if(pool != null) {
			pool.returnResource(jedisInstance);
		}
	}
	private void returnJedisResourcetofinal(String poolName, final Jedis jedisInstance) {
		CachePool pool = cachePool.get(poolName);
		if(pool != null) {
			pool.returnResource(jedisInstance);
		}
	}

	private Jedis getJedisResource(String instanceName) {
		Jedis jedisResource = null;
		
		CachePool pool = cachePool.get(instanceName);
		if(pool != null) {
	
			jedisResource = pool.getResource();
			
		}
		return jedisResource;
	}
	

	public enum CachePoolName {
		upload, 
		subpub,
	
		group_counter
	}
}
