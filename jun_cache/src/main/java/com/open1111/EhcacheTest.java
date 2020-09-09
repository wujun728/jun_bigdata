package com.open1111;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheTest {

	public static void main(String[] args) {
		// 根据ehcache.xml配置文件创建Cache管理器
		CacheManager manager=CacheManager.create("./src/main/resources/ehcache.xml");
		Cache c=manager.getCache("a"); // 获取指定cache
		Element e=new Element("java1234","牛逼");
		c.put(e); // 把一个元素添加到Cache中
		
		Element e2=c.get("java1234"); // 根据key获取缓存元素
		System.out.println(e2);
		System.out.println(e2.getObjectValue());
		
		c.flush(); // 刷新缓存
		manager.shutdown(); // 关闭缓存管理器
		
	}
}
