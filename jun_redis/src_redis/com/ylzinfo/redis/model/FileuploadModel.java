package com.ylzinfo.redis.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;


import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.ylzinfo.redis.manager.CacheManager;
import com.ylzinfo.redis.manager.CacheManager.CachePoolName;

import redis.clients.jedis.Jedis;

/*
 * 
 * @Description ֵ
 * @author Wujun
 *
 *
 */
public class FileuploadModel  implements Runnable {
	private String filename; 
	private CountDownLatch downLatch;  

	public FileuploadModel(CountDownLatch downLatch, String filename){
		this.downLatch = downLatch;  
		this.filename = filename;  
	}
	  public void run() {  
		 
		  Long filename = System.currentTimeMillis(); 
		  this.getCacheData(filename.toString());
	  }
	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
    public  synchronized static void readFiletoCache(File file) {
        if (file == null) {
            return;
        }

        CacheManager cacheManager = CacheManager.getInstance();
        Jedis uploadInstance = null;
        BufferedReader bd = null;
        try {
        	uploadInstance = cacheManager.getResource(CachePoolName.upload);
        	System.out.println("按行读取文件，一次读取一行:");
        	System.out.println("\n==========start reading==========");
        	bd = new BufferedReader(new FileReader(file));
        	String line = "";
        	String strmd5=FileuploadModel.getMd5ByFile(file);
        	Long start = System.currentTimeMillis(); 
        	System.out.println("开始时间"+start);

        	   while ((line = bd.readLine()) != null) {
        		int key=0;
        	    if(line.length() > 0) {
        	    	System.out.println(line);
        	    	uploadInstance.zadd(strmd5,key, line);
        	    	uploadInstance.expire(strmd5, 120);
        	    }
        	    key++;
        	   }
           	Long end = System.currentTimeMillis(); 
        	System.out.println(strmd5+"截止时间"+end);

        } catch (Exception e) {
        } finally {
            cacheManager.returnResource(CachePoolName.upload, uploadInstance);
        }

    }
    @SuppressWarnings("resource")
	public   static void getCacheData(String filename) {
     
    	Random random = new Random(100);
        CacheManager cacheManager = CacheManager.getInstance();
        Jedis uploadInstance = null;
        BufferedReader bd = null;
        try {
        	uploadInstance = cacheManager.getResource(CachePoolName.upload);
        
        	System.out.println("\n==========start writing==========");
        	Set<String> set=uploadInstance.zrevrange ("9c3c8af871281c2b6d07bbe9e27b6a4c", 0, -1);
        	Long start = System.currentTimeMillis(); 
        	System.out.println("开始时间"+start);
        	File file = new File("d:/"+filename+random.nextInt()+".txt");
            FileWriter fw = null;
            BufferedWriter writer = null;

        	Iterator<String> it = set.iterator();
        	fw = new FileWriter(file);
        	writer = new BufferedWriter(fw);
        	while(it.hasNext()){
        		writer.write(it.next().toString());
                writer.newLine();//换行
    			//System.out.println(((String)it.next()));
    		}
        	  writer.flush();
        	Long end = System.currentTimeMillis(); 
        	System.out.println(end-start);

        } catch (Exception e) {
        } finally {
            cacheManager.returnResource(CachePoolName.upload, uploadInstance);
        }

    }
public  static void main(String arg[]){
	 File file=new File("D:/abcd.txt");
	 try {
		 
		FileuploadModel.readFiletoCache(file);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
}
