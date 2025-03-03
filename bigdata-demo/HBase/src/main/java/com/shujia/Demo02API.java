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
import java.util.List;

public class Demo02API {
    Connection conn;

    @Before
    public void init() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        conn = ConnectionFactory.createConnection(conf);
    }


    @Test
    // create table
    public void createTable() throws IOException {
        Admin admin = conn.getAdmin();

        HTableDescriptor testAPI = new HTableDescriptor(TableName.valueOf("testAPI"));

        // 创建列簇
        HColumnDescriptor cf1 = new HColumnDescriptor("cf1");
        // 对列簇进行配置
        cf1.setMaxVersions(3);

        // 给testAPI表添加一个列簇
        testAPI.addFamily(cf1);

        // 创建testAPI表
        admin.createTable(testAPI);
    }

    @Test
    // list 查看所有表
    public void listTables() throws IOException {
        Admin admin = conn.getAdmin();

        TableName[] tableNames = admin.listTableNames();

        for (TableName tableName : tableNames) {
            System.out.println(tableName.getNameAsString());
        }
    }

    @Test
    // desc 查看表结构
    public void getTableDescriptor() throws IOException {
        Admin admin = conn.getAdmin();

        HTableDescriptor testAPI = admin.getTableDescriptor(TableName.valueOf("testAPI"));

        HColumnDescriptor[] cfs = testAPI.getColumnFamilies();

        for (HColumnDescriptor cf : cfs) {
            System.out.println(cf.getNameAsString());
            System.out.println(cf.getMaxVersions());
            System.out.println(cf.getTimeToLive());
        }


    }

    @Test
    // alter
    // 对testAPI 将cf1的版本设置为5，并且新加一个列簇cf2
    public void AlterTable() throws IOException {
        Admin admin = conn.getAdmin();
        TableName testAPI = TableName.valueOf("testAPI");

        HTableDescriptor testAPIDesc = admin.getTableDescriptor(testAPI);

        HColumnDescriptor[] cfs = testAPIDesc.getColumnFamilies();
        for (HColumnDescriptor cf : cfs) {
            if ("cf1".equals(cf.getNameAsString())) {
                cf.setMaxVersions(5);
            }
        }

        testAPIDesc.addFamily(new HColumnDescriptor("cf2"));


        admin.modifyTable(testAPI, testAPIDesc);


    }

    @Test
    // drop
    public void DropTable() throws IOException {
        Admin admin = conn.getAdmin();
        TableName tableName = TableName.valueOf("test1");
        if (admin.tableExists(tableName)) {
            // 表在删除之前需要先disable
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } else {
            System.out.println("表不存在！");
        }
    }


    @Test
    // put
    public void PutData() throws IOException {
        Table testAPI = conn.getTable(TableName.valueOf("testAPI"));

        Put put = new Put("001".getBytes());
        put.addColumn("cf1".getBytes(), "name".getBytes(), "张三".getBytes());
        put.addColumn("cf1".getBytes(), "age".getBytes(), "18".getBytes());
        put.addColumn("cf1".getBytes(), "clazz".getBytes(), "文科一班".getBytes());
        put.addColumn("cf1".getBytes(), "clazz".getBytes(), 1, "文科二班".getBytes());

        testAPI.put(put);


    }

    @Test
    // get
    public void GetData() throws IOException {
        Table testAPI = conn.getTable(TableName.valueOf("testAPI"));

        Get get = new Get("001".getBytes());

        get.setMaxVersions(10);

        Result rs = testAPI.get(get);

        // 获取rowkey
        byte[] row = rs.getRow();

        byte[] name = rs.getValue("cf1".getBytes(), "name".getBytes());
        byte[] age = rs.getValue("cf1".getBytes(), "age".getBytes());
        byte[] clazz = rs.getValue("cf1".getBytes(), "clazz".getBytes());

        System.out.println(Bytes.toString(row) + "," + Bytes.toString(name) + "," + Bytes.toString(age) + "," + Bytes.toString(clazz));

    }

    @Test
    // 提取数据的另一种方式
    public void ListCells() throws IOException {
        Table testAPI = conn.getTable(TableName.valueOf("testAPI"));

        Get get = new Get("001".getBytes());

        get.setMaxVersions(10);

        Result rs = testAPI.get(get);

        // 获取所有的Cell
        List<Cell> cells = rs.listCells();

        for (Cell cell : cells) {
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            System.out.println(value);
        }


    }

    @Test
    /**
     * 创建stu表，增加一个info列簇，将students.txt的1000条数据全部插入
     */
    public void PutStu() throws IOException {
        TableName stu = TableName.valueOf("stu");

        // 创建表
        Admin admin = conn.getAdmin();
        if (!admin.tableExists(stu)) {
            admin.createTable(new HTableDescriptor(stu)
                    .addFamily(new HColumnDescriptor("info")));
        }

        Table stuTable = conn.getTable(stu);

        ArrayList<Put> puts = new ArrayList<>();

        // 读取文件
        BufferedReader br = new BufferedReader(new FileReader("data/students.txt"));
        int cnt = 0;
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            String id = split[0];
            String name = split[1];
            String age = split[2];
            String gender = split[3];
            String clazz = split[4];

            Put put = new Put(id.getBytes());
            put.addColumn("info".getBytes(), "name".getBytes(), name.getBytes());
            put.addColumn("info".getBytes(), "age".getBytes(), age.getBytes());
            put.addColumn("info".getBytes(), "gender".getBytes(), gender.getBytes());
            put.addColumn("info".getBytes(), "clazz".getBytes(), clazz.getBytes());

            // 批量插入
            puts.add(put);
            cnt += 1;
            if (cnt == 100) {
                stuTable.put(puts);
                puts.clear(); // 清空
                cnt = 0;
            }
            // 逐条插入，效率较低
            //            stuTable.put(put);
        }

        // 判断Put的List是否为空
        if (!puts.isEmpty()) {
            stuTable.put(puts);
        }


        br.close();


    }


    @Test
    // delete
    public void DeleteData() throws IOException {
        Table stuTable = conn.getTable(TableName.valueOf("stu"));

        Delete del = new Delete("1500100001".getBytes());
        stuTable.delete(del);


    }

    @Test
    // scan
    public void ScanData() throws IOException {
        Table stuTable = conn.getTable(TableName.valueOf("stu"));

        Scan scan = new Scan();
        scan.setLimit(10);
        scan.withStartRow("1500100008".getBytes());
        scan.withStopRow("1500100020".getBytes());

        ResultScanner scanner = stuTable.getScanner(scan);
        for (Result rs : scanner) {
            String id = Bytes.toString(rs.getRow());
            String name = Bytes.toString(rs.getValue("info".getBytes(), "name".getBytes()));
            String age = Bytes.toString(rs.getValue("info".getBytes(), "age".getBytes()));
            String gender = Bytes.toString(rs.getValue("info".getBytes(), "gender".getBytes()));
            String clazz = Bytes.toString(rs.getValue("info".getBytes(), "clazz".getBytes()));
            System.out.println(id + "," + name + "," + age + "," + gender + "," + clazz);
        }

    }


    @After
    public void close() throws IOException {
        conn.close();
    }


}
