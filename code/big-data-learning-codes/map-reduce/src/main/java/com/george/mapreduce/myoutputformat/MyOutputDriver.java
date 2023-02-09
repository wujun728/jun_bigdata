package com.george.mapreduce.myoutputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/25 0:02
 * @since JDK 1.8
 */
public class MyOutputDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(MyOutputDriver.class);
        job.setOutputFormatClass(MyOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/outputformat/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/outputformat/output"));

        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();
        System.out.println("执行结果: " + successful);
    }
}
