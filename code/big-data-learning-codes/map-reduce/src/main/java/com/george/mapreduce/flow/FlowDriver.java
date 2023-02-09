package com.george.mapreduce.flow;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 *     流量统计 Driver类
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/27 21:54
 * @since JDK 1.8
 */
@Component
public class FlowDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDriver.class);
    // namenode地址
    @Value("${hadoop.name-node}")
    private String hdfsUrl;

    // 操作用户
    @Value("${hadoop.username}")
    private String hdfsUser;

    /**
     * 流量统计方法
     * @param inputPath 文件输入路径
     * @param outputPath 文件输出路径
     * @return
     */
    public int flowStatistics(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        // 远程调试时需要设置hadoop用户名
        System.setProperty("HADOOP_USER_NAME", hdfsUser);

        // 1、获取配置信息及封装任务
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        Job job = Job.getInstance(configuration);

        // 2、设置jar路径
        job.setJarByClass(FlowDriver.class);

        // 3、设置map和reduce类
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReduce.class);

        // 4、设置map输出K V类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        // 5、设置reduce输出K V类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 6、设置输入路径和输出路径
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 7、提交Job
        boolean result = job.waitForCompletion(true);
        LOGGER.info("任务提交结果：{}", job.isSuccessful());
        return job.isSuccessful() ? 0 : 1;
    }
}
