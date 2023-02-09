package com.shujia.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Demo5MRFilter {
    public static class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            // 过滤出文科三班的学生
            String clazz = value.toString().split(",")[4];
            if ("文科三班".equals(clazz)) {
                context.write(value, NullWritable.get());
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        // 设置MapReduce输出的K-V的分隔符
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf);
        job.setJobName("Demo5MRFilter");
        job.setJarByClass(Demo5MRFilter.class);

        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 配置输入输出路径
        FileInputFormat.addInputPath(job, new Path("/student/input"));
        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/student/filter/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

        // 等待job运行完成
        job.waitForCompletion(true);

        /**
         * hdfs dfs -mkdir -p /student/filter/output
         * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo5MRFilter
         */

    }
}
