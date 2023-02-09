package com.george.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/2 20:20
 * @since JDK 1.8
 */
@SpringBootTest
public class HdfsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsTest.class);
    FileSystem fs = null;
    /**
     * hadoop hdfs 上传路径
     */
    @Value("${hadoop.name-node}")
    private String hdfsUrl;

    /**
     * 操作用户
     */
    @Value("${hadoop.username}")
    private String hdfsUser;


    @Test
    public void test() {
        System.out.println(1 + 1);
    }

    @BeforeEach
    public void before() {
        if (fs == null) {
            Configuration configuration = new Configuration();
            try {
                fs = FileSystem.get(new URI(hdfsUrl), configuration, hdfsUser);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testClient() throws IOException {
        boolean mkdirs = fs.mkdirs(new Path("/111/test/"));
        LOGGER.info("创建文件夹 {}", mkdirs);
    }

    /**
     * HDFS文件上传（测试参数优先级）
     * 参数优先级排序：（1）客户端代码中设置的值 >（2）ClassPath下的用户自定义配置文件 >（3）然后是服务器的默认配置
     */
    @Test
    public void testCopyFromLocalFile() throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        // 设备副本数
        configuration.set("dfs.replication", "2");
        // 获取文件系统
        FileSystem fileSystem = FileSystem.get(new URI(hdfsUrl), configuration, hdfsUser);
        // 文件上传
        fileSystem.copyFromLocalFile(new Path("D:/George/phone_data.txt"), new Path("/hadoop/test"));
        // 关闭文件系统
        fileSystem.close();
    }

    /**
     * HDFS文件下载
     * @throws IOException
     */
    @Test
    public void testCopyToLocalFile() throws IOException {
        // 执行下载操作
        // boolean delSrc 指是否将原文件删除
        // Path src 指要下载的文件路径
        // Path dst 指将文件下载到的路径
        // boolean useRawLocalFileSystem 是否开启文件校验
        fs.copyToLocalFile(false, new Path("/hadoop/test/123.txt"), new Path("F:\\tmp\\456.txt"), true);
    }

    /**
     * HDFS文件夹删除
     */
    @Test
    public void testDelete() throws IOException {
        fs.delete(new Path("/client/test"), true);
    }

    /**
     * HDFS文件名更改
     */
    @Test
    public void testRename() throws IOException {
        fs.rename(new Path("/hadoop/test/123.txt"), new Path("/hadoop/test/456.txt"));
    }

    /**
     * HDFS文件详情查看
     * 查看文件名称、权限、长度、块信息
     * @throws IOException
     */
    @Test
    public void testListFiles() throws IOException {
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while(listFiles.hasNext()){
            LocatedFileStatus status = listFiles.next();

            // 输出详情
            // 文件名称
            System.out.println(status.getPath().getName());
            // 长度
            System.out.println(status.getLen());
            // 权限
            System.out.println(status.getPermission());
            // 分组
            System.out.println(status.getGroup());

            // 获取存储的块信息
            BlockLocation[] blockLocations = status.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                // 获取块存储的主机节点
                String[] hosts = blockLocation.getHosts();
                for (String host : hosts) {
                    System.out.println(host);
                }
            }
            System.out.println("-----------          ----------");
        }
    }

    /**
     * HDFS文件和文件夹判断
     * @throws IOException
     */
    @Test
    public void testListStatus() throws IOException {
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("f:"+fileStatus.getPath().getName());
            }else {
                System.out.println("d:"+fileStatus.getPath().getName());
            }
        }
    }

    /**
     * HDFS文件上传(I/O流操作)
     */
    @Test
    public void putFileToHDFS() throws IOException {
        // 创建输入流
        FileInputStream fis = new FileInputStream(new File("F:\\tmp\\456.txt"));
        // 获取输出流
        FSDataOutputStream fos = fs.create(new Path("/456.txt"));
        // 流对拷
        IOUtils.copyBytes(fis, fos, new Configuration());
        // 关闭资源
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fis);
    }

    /**
     * HDFS文件下载(I/O流操作)
     */
    @Test
    public void getFileFromHDFS() throws IOException {
        // 获取输入流
        FSDataInputStream fis = fs.open(new Path("/456.txt"));
        // 获取输出流
        FileOutputStream fos = new FileOutputStream(new File("F:\\tmp\\456.txt"));
        // 流的对拷
        IOUtils.copyBytes(fis, fos, new Configuration());
        // 关闭资源
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fis);
    }

    /**
     * 定位文件读取
     * 需求：分块读取HDFS上的大文件，比如根目录下的/hadoop-2.7.2.tar.gz
     * 下载第一块
     */
    @Test
    public void readFileSeek1() throws IOException {
        // 创建输入流
        FSDataInputStream fis = fs.open(new Path("/hadoop/test/hadoop-2.7.2.tar.gz"));
        // 创建输出流
        FileOutputStream fos = new FileOutputStream(new File("F:/tmp/hadoop-2.7.2.tar.gz.part1"));
        // 流拷贝
        byte[] buf = new byte[1024];
        for(int i =0 ; i < 1024 * 128; i++){
            fis.read(buf);
            fos.write(buf);
        }
        // 关闭资源
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
    }

    /**
     * 下载第二块
     */
    @Test
    public void readFileSeek2() throws IOException {
        // 打开输入流
        FSDataInputStream fis = fs.open(new Path("/hadoop/test/hadoop-2.7.2.tar.gz"));
        // 定位输入数据位置
        fis.seek(1024*1024*128);
        // 创建输出流
        FileOutputStream fos = new FileOutputStream(new File("F:/tmp/hadoop-2.7.2.tar.gz.part2"));
        // 流的对拷
        IOUtils.copyBytes(fis, fos, new Configuration());
        // 关闭资源
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
    }

    @AfterEach
    public void after() {
        if (null != fs) {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
