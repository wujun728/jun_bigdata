package com.ylzinfo.redis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ylzinfo.redis.model.FileuploadModel;

public class testCountDownLatch {
	public static void main(String[] args) {  
        ExecutorService executor = Executors.newFixedThreadPool(100);  
          
        CountDownLatch latch = new CountDownLatch(3);  
          
        FileuploadModel w1 = new FileuploadModel(latch,"app1");  
        FileuploadModel w2 = new FileuploadModel(latch,"app2");  
        FileuploadModel w3 = new FileuploadModel(latch,"app3");  
        FileuploadModel w4 = new FileuploadModel(latch,"app3");  
        FileuploadModel w5 = new FileuploadModel(latch,"app3");  

        executor.execute(w5);   

        executor.execute(w4);   
        executor.execute(w3);  
        executor.execute(w2);  
        executor.execute(w1);  
      
        executor.shutdown();  
    }  
  
}
