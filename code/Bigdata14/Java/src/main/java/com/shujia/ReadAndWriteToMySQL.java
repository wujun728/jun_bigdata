package com.shujia;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadAndWriteToMySQL {
    /**
     * 手动建表：
     * CREATE TABLE `student_like` (
     * `id` int(10) NOT NULL COMMENT '学生id',
     * `name` varchar(10) DEFAULT NULL COMMENT '学生姓名',
     * `age` int(2) DEFAULT NULL COMMENT '学生年龄',
     * `gender` varchar(1) DEFAULT NULL COMMENT '学生性别',
     * `clazz` varchar(5) DEFAULT NULL COMMENT '学生班级'
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生信息表';
     */
    public static void main(String[] args) throws Exception {
        // 读取student.txt文件
        BufferedReader br = new BufferedReader(new FileReader("Java/data/students.txt"));
        String line;
        ArrayList<String[]> linesArr = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            // 避免重复读取数据，在第一次读的时候将每一行数据切分后放入ArrayList
            linesArr.add(line.split(","));
        }
        br.close();

        // 将创建连接的代码放在循环外，避免创建多次连接
//        // 1、加载驱动
//        Class.forName("com.mysql.jdbc.Driver");

//        // 2、创建连接
//        Connection conn = DriverManager.getConnection("jdbc:mysql://master:3306/db1?useSSL=false", "root", "123456");
//

        // 使用线程池
        ExecutorService es = Executors.newFixedThreadPool(8);

        // 使用连接池
        // 配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://master:3306/db1");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("connectionTimeout", "10000"); // 连接超时：1秒
        config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
        config.addDataSourceProperty("maximumPoolSize", "10"); // 最大连接数：10
        config.addDataSourceProperty("useSSL", "false"); // 关闭使用SSL连接

        // 创建连接池
        HikariDataSource ds = new HikariDataSource(config);

        // show global status like "%Thread%"; 查看MySQL的连接数

        for (int i = 0; i < 100; i++) {
            // 每次从连接池中获取一个连接
            Connection conn = ds.getConnection();

            System.out.println("第" + i + "次插入");
            InsertTask task = new InsertTask(conn, linesArr, i);
            es.submit(task);
        }

        // 销毁线程池
        es.shutdown();
        // 销毁连接池
        ds.close();

        // 关闭连接
//        conn.close();
    }
}
