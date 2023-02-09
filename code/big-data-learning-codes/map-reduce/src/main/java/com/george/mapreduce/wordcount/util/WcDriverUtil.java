package com.george.mapreduce.wordcount.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/17 13:03
 * @since JDK 1.8
 */
@Component
public class WcDriverUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WcDriver.class);

    // NameNode路径
    @Value("${hadoop.name-node}")
    private String hdfsUrl;

    /**
     * 操作用户
     */
    @Value("${hadoop.username}")
    private String hdfsUser;

    public int wordCount(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        // 远程调试时需要设置hadoop用户名
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        // 1、获取配置信息及封装任务
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        Job job = Job.getInstance(configuration);
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);// 4m
        CombineTextInputFormat.setMinInputSplitSize(job, 2097152);// 2m
        // 2、设置jar路径
        job.setJarByClass(WcDriverUtil.class);

        // 3、设置map和reduce类
        job.setMapperClass(WcMapper.class);
        job.setReducerClass(WcReducer.class);

        // 4、设置map输出K V类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5、设置reduce（最终）输出的 K V类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6、设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 7、提交Job
        boolean result = job.waitForCompletion(true);
        LOGGER.info("任务提交结果：{}", job.isSuccessful());
        return job.isSuccessful() ? 0 : 1;
    }
}
