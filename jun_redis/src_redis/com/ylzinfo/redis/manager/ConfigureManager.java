package com.ylzinfo.redis.manager;


import java.util.Properties;

import com.ylzinfo.redis.config.Configure;


public class ConfigureManager
{


	private static ConfigureManager _configureManagerInstance =  new ConfigureManager();
	
	public static ConfigureManager getInstance()
	{
		if(_configureManagerInstance == null)
		{
			_configureManagerInstance = new ConfigureManager();
		}
		return _configureManagerInstance;
	}
	
	private Configure configure;
	
	private ConfigureManager()
	{
		configure = new Configure();
	}
	

	public void initial() throws Exception
	{
		reloadAllConfigs();
	}
	


	public Properties getCacheConfig()
	{
		return configure.getCacheConfig();
	}
	

	public Properties getSystemConfig()
	{
		return configure.getSystemConfig();
	}
	

	

	
	public void loadAllConfigs() throws Exception
	{
		configure.loadConfigs();
	}
	
	
	public void reloadAllConfigs() throws Exception {
		loadAllConfigs();
	}

}
