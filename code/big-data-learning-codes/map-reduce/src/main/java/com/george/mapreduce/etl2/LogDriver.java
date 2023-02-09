package com.george.mapreduce.etl2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/16 22:30
 * @since JDK 1.8
 */
public class LogDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(LogDriver.class);

        job.setMapperClass(LogMapper.class);

        job.setNumReduceTasks(0);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/etl/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/etl/output"));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
