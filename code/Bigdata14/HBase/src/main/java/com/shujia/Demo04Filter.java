package com.shujia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class Demo04Filter {
    Connection conn;
    Table stu;

    public ResultScanner getScannerWithFilter(Filter filter) throws IOException {
        Scan scan = new Scan();
        scan.setFilter(filter);
        return stu.getScanner(scan);
    }

    public void printScanner(Filter filter) throws IOException {
        for (Result rs : getScannerWithFilter(filter)) {
            String rk = Bytes.toString(rs.getRow());
            String name = Bytes.toString(rs.getValue("info".getBytes(), "name".getBytes()));
            String age = Bytes.toString(rs.getValue("info".getBytes(), "age".getBytes()));
            String gender = Bytes.toString(rs.getValue("info".getBytes(), "gender".getBytes()));
            String clazz = Bytes.toString(rs.getValue("info".getBytes(), "clazz".getBytes()));
            System.out.println(rk + "," + name + "," + age + "," + gender + "," + clazz);
        }
    }

    // 使用CellUtil进行打印
    public void printScannerWithCellUtil(Filter filter) throws IOException {
        for (Result rs : getScannerWithFilter(filter)) {
            for (Cell cell : rs.listCells()) {
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                System.out.println(rowkey + "," + value);
            }
        }

    }

    @Before
    public void init() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        conn = ConnectionFactory.createConnection(conf);

        stu = conn.getTable(TableName.valueOf("stu"));
    }

    @Test
    // 过滤出Rowkey(id)中 包含8的数据
    public void RowFilterWithSubString() throws IOException {
        SubstringComparator comparator = new SubstringComparator("8");
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
        printScanner(rowFilter);
    }

    @Test
    // 过滤出 列簇名为cf2下的所有列的数据
    public void FamilyFilterWithCom() throws IOException {
        Scan scan = new Scan();

        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator("cf2".getBytes()));

        scan.setFilter(familyFilter);

        Table test2 = conn.getTable(TableName.valueOf("test2"));

        ResultScanner sc = test2.getScanner(scan);

        for (Result rs : sc) {
            for (Cell cell : rs.listCells()) {
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                System.out.println(rowkey + "," + value);
            }
        }


    }

    @Test
    // stu表中列名包含a的所有的列的数据，使用正则比较器
    public void QualifierFilterWithRegex() throws IOException {
        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(".*a.*"));

        printScannerWithCellUtil(qualifierFilter);
    }

    @Test
    // 过滤出 数据中包含 文 的所有数据
    public void ValueFilterWithSubString() throws IOException {
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL,
                new SubstringComparator("文"));

        printScannerWithCellUtil(valueFilter);
    }

    @Test
    // 过滤出班级是 文科班 的学生的所有信息
    public void SingleColumnValueFilterWithBinaryPrefix() throws IOException {
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("info".getBytes()
                , "clazz".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , new BinaryPrefixComparator("文科".getBytes()));

        printScanner(singleColumnValueFilter);
    }

    @Test
    // 过滤出班级是 文科班 的学生的所有信息，最终结果没有 clazz 列
    public void SingleColumnValueExcludeFilterWithBinaryPrefix() throws IOException {
        SingleColumnValueExcludeFilter singleColumnValueExcludeFilter = new SingleColumnValueExcludeFilter("info".getBytes()
                , "clazz".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , new BinaryPrefixComparator("文科".getBytes()));

        printScanner(singleColumnValueExcludeFilter);
    }

    @Test
    // 过滤出年龄是 奇数 的学生的所有信息
    public void SingleColumnValueFilterWithRegex() throws IOException {
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("info".getBytes()
                , "age".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , new RegexStringComparator("^[0-9]{0,1}[13579]$"));

        printScanner(singleColumnValueFilter);
    }

    @Test
    // 查询以150010008开头的所有前缀的rowkey
    public void PrefixFilter() throws IOException {
        // 第一种方式
        PrefixFilter prefixFilter = new PrefixFilter("150010008".getBytes());

        printScanner(prefixFilter);

        System.out.println("**********************************");
        // 第二种方式
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryPrefixComparator("150010008".getBytes()));

        printScanner(rowFilter);

    }

    @Test
    // 过滤出 理科班 中的 女生 年龄为奇数 的学生的所有信息
    public void CombineFilter() throws IOException {
        SingleColumnValueFilter filter1 = new SingleColumnValueFilter("info".getBytes()
                , "clazz".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , new BinaryPrefixComparator("理科".getBytes()));

        SingleColumnValueExcludeFilter filter2 = new SingleColumnValueExcludeFilter("info".getBytes()
                , "gender".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , "女".getBytes());

        SingleColumnValueFilter filter3 = new SingleColumnValueFilter("info".getBytes()
                , "age".getBytes()
                , CompareFilter.CompareOp.EQUAL
                , new RegexStringComparator("^[0-9]{0,1}[13579]$"));

        /**
         * MUST_PASS_ALL ===>  and
         * MUST_PASS_ONE ===>  or
         */
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(filter1);
        filterList.addFilter(filter2);
        filterList.addFilter(filter3);

        printScanner(filterList);

    }


    @After
    public void close() throws IOException {
        conn.close();
    }
}
