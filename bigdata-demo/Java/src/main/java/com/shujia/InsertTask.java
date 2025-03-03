package com.shujia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertTask implements Runnable {
    Connection conn;
    ArrayList<String[]> linesArr;
    int i;

    public InsertTask(Connection conn, ArrayList<String[]> linesArr, int i) {
        this.conn = conn;
        this.linesArr = linesArr;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("正在执行第" + i + "个任务");

        try {

            // 3、创建prepareStatement
            PreparedStatement ps = conn.prepareStatement("insert into student_like values(?,?,?,?,?)");

            for (String[] splits : linesArr) {
                // splits就是每一行数据都切好
                // 例如：1500100988,余鸿云,22,男,文科六班
                String id = splits[0];
                String name = splits[1];
                String age = splits[2];
                String gender = splits[3];
                String clazz = splits[4];

                // 设置参数
                ps.setInt(1, Integer.parseInt(id));
                ps.setString(2, name);
                ps.setInt(3, Integer.parseInt(age));
                ps.setString(4, gender);
                ps.setString(5, clazz);

                // 执行SQL
//            ps.executeUpdate(); // 直接在while循环中执行 相当于总共执行乐1000次insert
                // 使用批量插入
                ps.addBatch(); // 将语句先放入Batch中
            }

//            while ((line = br.readLine()) != null) {
//                // line就是每一行数据
//                // 例如：1500100988,余鸿云,22,男,文科六班
//                String[] splits = line.split(",");
//                String id = splits[0];
//                String name = splits[1];
//                String age = splits[2];
//                String gender = splits[3];
//                String clazz = splits[4];
//
//                // 设置参数
//                ps.setInt(1, Integer.parseInt(id));
//                ps.setString(2, name);
//                ps.setInt(3, Integer.parseInt(age));
//                ps.setString(4, gender);
//                ps.setString(5, clazz);
//
//                // 执行SQL
////            ps.executeUpdate(); // 直接在while循环中执行 相当于总共执行乐1000次insert
//                // 使用批量插入
//                ps.addBatch(); // 将语句先放入Batch中
//            }

            ps.executeBatch(); // 在循环外 进行批量处理
            ps.close();
            conn.close(); // 关闭连接池传入的连接并不会真正关闭，而是放回连接池
            System.out.println("第" + i + "个任务执行完成");
        } catch (Exception e) {
            System.out.println("发生了异常" + e);
        }
    }

}
