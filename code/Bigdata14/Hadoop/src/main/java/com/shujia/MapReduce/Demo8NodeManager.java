package com.shujia.MapReduce;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Demo8NodeManager {
    // 接收AM发送过来的Task并执行
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 创建Socket服务端
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("NodeManager已经启动，等待接收任务");

        // 建立Socket连接
        Socket socket = serverSocket.accept();

        // 创建输入流
        InputStream inputStream = socket.getInputStream();

        // 将输入流转换为Object输入流
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // 直接从Object输入流获取Object对象
        Object taskObj = objectInputStream.readObject();

        System.out.println("接收到了AM发送的Task");

        // 将Object对象转换为Task对象
        Task task = (Task) taskObj;

        System.out.println("正在执行Task");
        // 执行Task
        task.start();


        // 关闭流，断开连接
        objectInputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();


    }
}
