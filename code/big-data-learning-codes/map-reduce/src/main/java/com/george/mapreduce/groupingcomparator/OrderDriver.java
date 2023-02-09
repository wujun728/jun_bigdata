package com.george.mapreduce.groupingcomparator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
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
 * @date 2020/11/10 16:02
 * @since JDK 1.8
 */
public class OrderDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDriver.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(OrderDriver.class);
        job.setMapperClass(OrderSortMapper.class);
        job.setReducerClass(OrderSortReducer.class);

        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 设置分组比较
        job.setGroupingComparatorClass(OrderGroupingComparator.class);

        FileInputFormat.addInputPath(job, new Path("D:/test/hadoop/groupingcomparator/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/groupingcomparator/output"));

        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();

        LOGGER.info("执行结果 ===> {}", successful);
    }
}
