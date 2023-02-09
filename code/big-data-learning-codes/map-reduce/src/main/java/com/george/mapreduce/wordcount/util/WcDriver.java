package com.george.mapreduce.wordcount.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/17 10:37
 * @since JDK 1.8
 */
@Component
public class WcDriver extends Configured implements Tool {
    private static final Logger LOGGER = LoggerFactory.getLogger(WcDriver.class);

    // NameNode路径
    @Value("${hadoop.name-node}")
    private String hdfsUrl;

    @Override
    public int run(String[] args) throws Exception {
        // 1、获取配置信息及封装任务
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        Job job = Job.getInstance(configuration);
        // 2、设置jar路径
        job.setJarByClass(WcDriver.class);

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
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7、提交Job
        boolean result = job.waitForCompletion(true);
        LOGGER.info("任务提交结果：{}", job.isSuccessful());
        return job.isSuccessful() ? 0 : 1;
    }
}
