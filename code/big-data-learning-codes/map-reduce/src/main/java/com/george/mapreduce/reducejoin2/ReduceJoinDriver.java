package com.george.mapreduce.reducejoin2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/9 23:48
 * @since JDK 1.8
 */
public class ReduceJoinDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(ReduceJoinDriver.class);

        job.setMapperClass(ReduceJoinMapper.class);
        job.setReducerClass(ReduceJoinReducer.class);

        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        job.setGroupingComparatorClass(ReduceJoinComparator.class);

        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/reducejoin2/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/reducejoin2/output"));

        job.waitForCompletion(true);

        boolean successful = job.isSuccessful();
        System.out.println("任务执行结果：" + successful);
    }
}
