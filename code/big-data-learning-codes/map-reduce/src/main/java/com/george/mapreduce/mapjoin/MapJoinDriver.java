package com.george.mapreduce.mapjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/16 21:32
 * @since JDK 1.8
 */
public class MapJoinDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、获取Job实例
        Job job = Job.getInstance(new Configuration());
        // 2、设置加载Jar包信息
        job.setJarByClass(MapJoinDriver.class);
        // 3、设置Map Class
        job.setMapperClass(MapJoinMapper.class);
        // 4、设置ReduceTask数量
        job.setNumReduceTasks(0);
        // 5、加载缓存数据
        job.addCacheFile(URI.create("file:///D:/test/hadoop/mapjoin/input/pd.txt"));
        // 6、设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/mapjoin/input/order.txt"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/mapjoin/output"));
        // 7、提交任务
        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();
        System.out.println("执行结果：" + successful);
    }
}
