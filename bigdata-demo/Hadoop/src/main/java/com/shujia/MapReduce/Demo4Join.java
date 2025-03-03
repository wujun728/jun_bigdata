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

public class Demo4Join {
    public static class MyMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context) throws IOException, InterruptedException {
            // 区分value到底是哪个文件的数据
            String v = value.toString();
            if (v.contains(",")) {
                // 学生数据
                String[] stuSplits = v.split(",");
                long id = Long.parseLong(stuSplits[0]);
                String name = stuSplits[1];
                String clazz = stuSplits[4];
                context.write(new LongWritable(id), new Text(name + "," + clazz + "|"));
            } else {
                // 总分数据
                String[] sumScoreSplit = v.split("\t");
                context.write(new LongWritable(Long.parseLong(sumScoreSplit[0])), new Text(sumScoreSplit[1] + "#"));
            }

        }
    }

    public static class MyReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Reducer<LongWritable, Text, LongWritable, Text>.Context context) throws IOException, InterruptedException {
            String stuV = "";
            String sumScoreV = "";
            for (Text value : values) {
                String v = value.toString();
                if (v.contains("|")) {
                    // 学生数据
                    stuV = v.replace("|", "");
                } else {
                    // 总分数据
                    sumScoreV = v.replace("#", "");
                }

            }
            context.write(key, new Text(stuV + "," + sumScoreV));

        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        // 设置MapReduce输出的K-V的分隔符
        conf.set("mapred.textoutputformat.separator", ",");
        Job job = Job.getInstance(conf);
        job.setJobName("Demo4Join");
        job.setJarByClass(Demo4Join.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        // 配置输入输出路径
        FileInputFormat.addInputPath(job, new Path("/student/input"));
        FileInputFormat.addInputPath(job, new Path("/student/score/output"));
        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/student/join/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

        // 等待job运行完成
        job.waitForCompletion(true);

        /**
         * 创建目录
         * hdfs dfs -mkdir -p /student/join/output
         * 提交任务
         * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo4Join
         */
    }
}
