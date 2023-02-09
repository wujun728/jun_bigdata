package com.shujia.HDFS;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class HDFSJavaAPI {
    FileSystem fs;

    @Before
    public void init() throws IOException {
        // 1、创建连接
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://master:9000");
        fs = FileSystem.get(conf);
    }

    // 创建目录(默认可以递归地创建目录)
    @Test
    public void mkdirs() throws IOException {
        fs.mkdirs(new Path("/e/f/g"));
    }

    // 删除目录
    @Test
    public void deleteDir() throws IOException {
        // 非递归的删除，相当于直接rm
//        fs.delete(new Path("/e"), false);
        // 递归地删除，相当于rm -r
        fs.delete(new Path("/e"), true);
    }

    // 移动文件
    @Test
    public void moveFile() throws IOException {
        fs.rename(new Path("/CentOS-7-x86_64-DVD-2009.iso"), new Path("/a/CentOS-7-x86_64-DVD-2009.iso"));
    }

    // 上传文件 put (move会删除文件)
    @Test
    public void putFile() throws IOException {
        fs.moveFromLocalFile(new Path("data/students.txt"), new Path("/a/students.txt"));
    }

    // 下载文件 get (copy不会)
    @Test
    public void getFile() throws IOException {
        fs.copyToLocalFile(new Path("/a/students.txt"), new Path("data/"));
    }


    // 读文件
    @Test
    public void readFromHDFS() throws IOException {
        FSDataInputStream fsDataInputStream = fs.open(new Path("/a/students.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(fsDataInputStream));

        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
        fsDataInputStream.close();


    }

    // 写文件
    @Test
    public void writeFile() throws IOException {
        Path path = new Path("/newFile1.txt");
        FSDataOutputStream fsDataOutputStream;
        // 判断文件是否存在
        if(fs.exists(path)){
            // 直接追加有问题 不常用
            fsDataOutputStream = fs.append(path);
        }else{
            fsDataOutputStream= fs.create(path);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream));

        bw.write("hello world");
        bw.newLine();
        bw.write("hadoop hadoop hive hbase");
        bw.newLine();
        bw.flush();

        bw.close();
        fsDataOutputStream.close();


    }


    @After
    public void closed() throws IOException {
        fs.close();
    }
}
