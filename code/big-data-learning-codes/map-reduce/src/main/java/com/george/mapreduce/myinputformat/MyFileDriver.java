package com.george.mapreduce.myinputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/30 19:53
 * @since JDK 1.8
 */
public class MyFileDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(MyFileDriver.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        job.setInputFormatClass(MyFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        // 设置输入的inputFormat
        FileInputFormat.setInputPaths(job, new Path("F:/input"));
        // 设置输出的outputFormat
        FileOutputFormat.setOutputPath(job, new Path("F:/output"));

        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();
        System.out.println("执行结果 ===>>> " + successful);
    }
}
