package com.shujia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Demo03DianXin {
    Connection conn;
    TableName dianXin;

    @Before
    public void init() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        conn = ConnectionFactory.createConnection(conf);
        dianXin = TableName.valueOf("dianXin");
    }

    @Test
    // create table
    public void createTable() throws IOException {
        Admin admin = conn.getAdmin();

        if (!admin.tableExists(dianXin)) {
            admin.createTable(new HTableDescriptor(dianXin)
                    .addFamily(new HColumnDescriptor("cf1")
                            .setMaxVersions(5)));
        } else {
            System.out.println("表已经存在！");
        }

    }

    @Test
    // 将数据写入HBase
    public void putALL() throws IOException {
        Table dx_tb = conn.getTable(dianXin);
        ArrayList<Put> puts = new ArrayList<>();
        int cnt = 0;
        int batchSize = 1000;

        BufferedReader br = new BufferedReader(new FileReader("data/DIANXIN.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            String mdn = split[0];
            String start_time = split[1];
            String lg = split[4];
            String lat = split[5];

            Put put = new Put(mdn.getBytes());
            put.addColumn("cf1".getBytes(), "lg".getBytes(), Long.parseLong(start_time), lg.getBytes());
            put.addColumn("cf1".getBytes(), "lat".getBytes(), Long.parseLong(start_time), lat.getBytes());

            puts.add(put);
            cnt += 1;

            if (cnt == batchSize) {
                dx_tb.put(puts);
                puts.clear();
                cnt = 0;
            }

        }

        if (!puts.isEmpty()) {
            dx_tb.put(puts);
        }


        br.close();


    }

    @Test
    // 根据mdn获取用户最新的3个位置
    public void getPositionByMdn() throws IOException {
        Table dx_tb = conn.getTable(dianXin);

        String mdn = "48049101CE9FC280703582E667DE3F3D947ABD37";

        Get get = new Get(mdn.getBytes());
        get.setMaxVersions(3);

        Result rs = dx_tb.get(get);
        ArrayList<String> lgArr = new ArrayList<>();
        ArrayList<String> latArr = new ArrayList<>();

        for (Cell cell : rs.listCells()) {
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            if ("lg".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                lgArr.add(value);
            } else if ("lat".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                latArr.add(value);
            }
        }

        for (int i = 0; i < 3; i++) {
            System.out.println(lgArr.get(i) + "," + latArr.get(i));
        }


    }


    @After
    public void close() throws IOException {
        conn.close();
    }
}
