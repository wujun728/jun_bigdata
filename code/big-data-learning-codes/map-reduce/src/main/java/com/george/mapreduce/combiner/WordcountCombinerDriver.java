package com.george.mapreduce.combiner;

import com.george.mapreduce.wordcount.util.WcMapper;
import com.george.mapreduce.wordcount.util.WcReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/10 9:39
 * @since JDK 1.8
 */
public class WordcountCombinerDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordcountCombinerDriver.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、获取配置
        Job job = Job.getInstance(new Configuration());

        // 2、设置jar路径
        job.setJarByClass(WordcountCombinerDriver.class);

        // 方案一，设置combiner
        job.setCombinerClass(WordcountCombiner.class);

        // 方案二，指定reduce作为combiner
        // job.setCombinerClass(WcReducer.class);

        // 3、设置map和reduce
        job.setMapperClass(WcMapper.class);
        job.setReducerClass(WcReducer.class);

        // 4、设置map输出K V类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5、设置reduce（最终）输出的 K V类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6、设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/combiner/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/combiner/output"));

        // 7、提交Job
        boolean result = job.waitForCompletion(true);
        LOGGER.info("任务提交结果：{}", job.isSuccessful());
    }
}
