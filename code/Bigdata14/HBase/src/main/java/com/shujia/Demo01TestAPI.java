package com.shujia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

public class Demo01TestAPI {
    public static void main(String[] args) throws IOException {
        // 1、创建配置文件，设置HBase的连接地址（ZK的地址）
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        // 2、建立连接
        Connection conn = ConnectionFactory.createConnection(conf);

        /**
         *  3、执行操作:
         *  对表的结构进行操作 则getAdmin
         *  对表的数据进行操作 则getTable
         */
        Admin admin = conn.getAdmin();

        Table test = conn.getTable(TableName.valueOf("test"));

        // 4、关闭连接
        conn.close();


    }
}
