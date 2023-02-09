package com.shujia;

import java.sql.*;

public class MySQLJDBCDemo {
    public static void main(String[] args) throws Exception {
        // 1、加载驱动
        Class.forName("com.mysql.jdbc.Driver");

        // 2、创建连接
        Connection conn = DriverManager.getConnection("jdbc:mysql://master:3306/db1?useSSL=false", "root", "123456");

//        String clz = "文科二班 or 1=1"; // 直接使用变量拼接SQL会造成SQL注入问题
        String clz = "文科二班"; // 直接使用变量拼接SQL会造成SQL注入问题
//        String ag = "23 or 1=1";
        int ag = 23;

////        // 3、创建Statement
//        Statement st = conn.createStatement();
////
////        // 4、通过Statement执行SQL
//        ResultSet rs = st.executeQuery("select * from student where age>"+ag);

        // 3、使用prepareStatement避免SQL注入问题
        // 执行DQL时使用executeQuery方法
        PreparedStatement ps = conn.prepareStatement("select * from student where clazz=? and age >?");
        // 4、通过PreparedStatement执行SQL
        // 先设置参数 从1开始编号
        ps.setString(1, clz);
        ps.setInt(2, ag);
        // 再执行SQL
        ResultSet rs = ps.executeQuery();


        // 5、遍历ResultSet 获取返回的记录
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String gender = rs.getString("gender");
            String clazz = rs.getString("clazz");

            System.out.println(id + "," + name + "," + age + "," + gender + "," + clazz);
        }
        // 执行DML(insert、update、delete)操作 可以使用executeUpdate方法
        int i = ps.executeUpdate("update score set score=100");
        System.out.println(i); // 返回受影响的记录的条数

        // 其他操作使用execute方法
        boolean bool = ps.execute("create database if not exists db3");
        System.out.println(bool); // 指示执行的SQL有无返回值
        boolean bool2 = ps.execute("select * from score where score<0 limit 1");
        System.out.println(bool2);


        // 6、关闭连接
//        st.close();
        ps.close();
        conn.close();


    }
}
