package com.shujia;

import java.sql.*;

public class Demo07PhoenixJDBC {
    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:phoenix:master,node1,node2:2181");
        PreparedStatement ps = conn.prepareStatement("select /*+ INDEX(DIANXIN DIANXIN_INDEX) */ * from DIANXIN where end_date=?");
        ps.setString(1, "20180503212649");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String mdn = rs.getString("mdn");
            String start_date = rs.getString("start_date");
            String end_date = rs.getString("end_date");
            String x = rs.getString("x");
            String y = rs.getString("y");
            String county = rs.getString("county");
            System.out.println(mdn + "\t" + start_date + "\t" + end_date + "\t" + x + "\t" + y + "\t" + county);
        }
        ps.close();
        conn.close();

    }
}
