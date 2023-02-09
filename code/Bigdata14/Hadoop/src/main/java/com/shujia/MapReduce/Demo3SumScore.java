package com.shujia.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Demo3SumScore {
    // Map端
    public static class MyMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, IntWritable>.Context context) throws IOException, InterruptedException {
            String[] splits = value.toString().split(",");
            String id = splits[0];
            String score = splits[2];
            // 以id作为key 分数score作为value
            context.write(new LongWritable(Long.parseLong(id)), new IntWritable(Integer.parseInt(score)));
        }
    }

    // Reduce端
    public static class MyReducer extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
        @Override
        protected void reduce(LongWritable key, Iterable<IntWritable> values, Reducer<LongWritable, IntWritable, LongWritable, IntWritable>.Context context) throws IOException, InterruptedException {
            int sumScore = 0;
            // 统计学生总分
            for (IntWritable value : values) {
                sumScore += value.get();
            }
            context.write(key, new IntWritable(sumScore));
        }
    }

    // Driver端
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("Demo3SumScore");
        job.setJarByClass(Demo3SumScore.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(IntWritable.class);

        // 配置输入输出路径
        FileInputFormat.addInputPath(job, new Path("/student/score/input"));
        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/student/score/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

        // 等待job运行完成
        job.waitForCompletion(true);

        /**
         * 1、准备数据，将students.txt上传至HDFS的/student/input目录下面
         * hdfs dfs -mkdir -p /student/score/input
         * hdfs dfs -put score.txt /student/score/input/
         * 2、提交MapReduce任务
         * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo3SumScore
         * 3、查看日志、杀死任务
         * yarn logs -applicationId application_1644480440500_0006
         * yarn application -kill application_1644480440500_0007
         */

    }
}
