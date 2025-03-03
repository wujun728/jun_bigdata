package com.shujia.MapReduce;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Demo7MRAppMaster {
    // 作为MR任务运行的AM，负责任务Task的分配与调度
    public static void main(String[] args) throws IOException {
        // 创建Socket客户端
        Socket sk = new Socket("localhost", 8888);

        // 创建Task任务，即将通过Socket发送给NM
        Task task = new Task();

        System.out.println("Task以构建，准备发送");
        // 建立输出流
        OutputStream outputStream = sk.getOutputStream();

        // 将输出流转换为Object的输出流
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // 直接将任务以Object的形式发送出去
        objectOutputStream.writeObject(task);

        objectOutputStream.flush();
        System.out.println("Task已发送成功");


        objectOutputStream.close();
        outputStream.close();
        sk.close();


    }

}

/**
 * 在MR中，MapTask、ReduceTask
 * 都是线程对象，因为需要在网络中传输，所以都实现了序列化接口
 * 分区、分组、排序等其他功能有MR框架提供
 */
class Task extends Thread implements Serializable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Task执行完毕");
    }
}
