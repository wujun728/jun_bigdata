package com.ylzinfo.redis.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author asartear
 * 
 */
public class Configure {

	public Properties systemConfig;

	public Properties cacheConfig;

	public Configure() {
		systemConfig = new Properties();
		cacheConfig = new Properties();
		try {
			loadConfigs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Properties getSystemConfig() {
		return systemConfig;
	}

	public Properties getCacheConfig() {
		return cacheConfig;
	}

	/**
	 * 加载缓存配置
	 * 
	 * @throws Exception
	 */
	public void loadConfigs() throws Exception {
		loadSystemConfig();
		loadCacheConfig();

	}
	/*
	 * 获取system.properties
	 */
	public void loadSystemConfig()
	{
		propertyConfigLoader_sys("/system.properties", systemConfig);
	}
	public void propertyConfigLoader_sys(String configFilePath, Properties config)
	{
		InputStream configFileStream = this.getClass().getResourceAsStream(configFilePath);
		try {
			
			config.load(configFileStream);
		} catch (IOException e) {
			
		}
	}
	/*
	 * 获取cache-online.properties
	 */
	public void loadCacheConfig() {
		String path =  systemConfig.getProperty("com.ylzinfo.config.file.cache");
		propertyConfigLoader(path, cacheConfig);
	}

	public void propertyConfigLoader(String configFilePath, Properties config) {
		if (configFilePath.length() == 0) {
			return;
		}
		InputStream configFileStream = this.getClass().getResourceAsStream(configFilePath);

		//File file = new File(configFilePath);
		//if (file.exists()) {
			try {
			//	InputStream configFileStream = new FileInputStream(file);
				config.load(configFileStream);
			} catch (IOException e) {
		//	}
		}

	}

}
