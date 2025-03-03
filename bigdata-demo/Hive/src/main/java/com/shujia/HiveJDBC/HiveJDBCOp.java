package com.shujia.HiveJDBC;

import java.sql.*;

public class HiveJDBCOp {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 1、加载驱动
        Class.forName("org.apache.hive.jdbc.HiveDriver");

        // 2、创建连接
        Connection conn = DriverManager.getConnection("jdbc:hive2://master:10000/test1","root","");

        // 3、创建Statement
//        Statement st = conn.createStatement();
        // 4、执行SQL语句 select * from students limit 10
//        ResultSet rs = st.executeQuery("select * from students limit 10");
        // 使用prepareStatement 防止SQL注入的问题
        PreparedStatement pSt = conn.prepareStatement("select * from students where clazz=?");
//         设置参数
        pSt.setString(1, "文科一班");

//        PreparedStatement pSt = conn.prepareStatement("select clazz,count(*) as cnt from students group by clazz");

        ResultSet rs = pSt.executeQuery();

        // 5、遍历ResultSet获取数据
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String gender = rs.getString("gender");
            String clazz = rs.getString("clazz");

            System.out.println(id + "," + name + "," + age + "," + gender + "," + clazz);
        }

        // 关闭连接
        rs.close();
        pSt.close();
        conn.close();
    }
}
