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

// 统计每个单词出现的次数
public class Demo1WordCount {
    // Map阶段
    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        /**
         * @param key     Map端输入的key->偏移量
         * @param value   Map端输入的value->一行数据
         * @param context MapReduce整个过程的上下文环境->可以获取MapReduce程序运行时的一些参数、状态，可以将Map的输出发送到Reduce
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 实现自己的map端逻辑
            String vStr = value.toString();
            // 按照空格进行切分，将每个单词切分出来
            String[] words = vStr.split(" ");

            // 遍历每一个单词，构造成k-v格式
            /**
             * hadoop hive hbase spark flink
             * ====>
             * hadoop 1
             * hive 1
             * hbase 1
             * spark 1
             * flink 1
             */
            for (String word : words) {
                Text keyOut = new Text(word);
                IntWritable valueOut = new IntWritable(1);
                // 通过context将构建好的k-v发送出去
                context.write(keyOut, valueOut);
            }

        }
    }

    // Reduce阶段
    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        /**
         * @param key     Map端输出的数据按照key进行分组过后的数据中的key，在这里相当于每个单词
         * @param values  Map端输出的数据按照key进行分组过后，相同key的所有的value组成的集合(迭代器)
         * @param context MapReduce的上下文环境，主要用于输出数据到HDFS
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 实现自己的Reduce逻辑
            int sum = 0; // 保存每个单词的数量
            for (IntWritable value : values) {
                // 遍历values迭代器
                sum += value.get();
            }

            // 将Reduce统计得到的结果输出到HDFS
            context.write(key, new IntWritable(sum));


        }
    }

    // Driver端（将Map、Reduce进行组装）
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        // 创建配置文件
        Configuration conf = new Configuration();

        // 创建一个Job实例
        Job job = Job.getInstance(conf);
        // 对Job进行一些简单的配置
        job.setJobName("Demo1WordCount");
        // 通过class类设置运行Job时该执行哪一个类
        job.setJarByClass(Demo1WordCount.class);

        // 对Map端进行配置
        // 对Map端输出的Key的类型进行配置
        job.setMapOutputKeyClass(Text.class);
        // 对Map端输出的Value的类型进行配置
        job.setMapOutputValueClass(IntWritable.class);
        // 配置Map任务该运行哪一个类
        job.setMapperClass(MyMapper.class);

        // 对Reduce端进行配置
        // 对Reduce端输出的Key的类型进行配置
        job.setOutputKeyClass(Text.class);
        // 对Reduce端输出的Value的类型进行配置
        job.setOutputValueClass(IntWritable.class);
        // 配置Reduce任务该运行哪一个类
        job.setReducerClass(MyReducer.class);

        // 配置输入输出路径
        FileInputFormat.addInputPath(job, new Path("/wordCount/input"));
        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/wordCount/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

        // 等待job运行完成
        job.waitForCompletion(true);

        /**
         * 1、准备数据，将words.txt上传至HDFS的/wordCount/input目录下面
         * hdfs dfs -mkdir -p /wordCount/input
         * hdfs dfs -put words.txt /wordCount/input
         * 2、提交MapReduce任务
         * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo1WordCount
         */

    }

}
