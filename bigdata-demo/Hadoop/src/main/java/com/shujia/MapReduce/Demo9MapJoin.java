package com.shujia.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

// 实现MapJoin
public class Demo9MapJoin {
    // Map端
    public static class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        Hashtable<String, String> sumScoreHTable = new Hashtable<>();

        // 每个Map任务会执行一次
        @Override
        protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException {
            // 获取”小表“的数据，构建Hashtable
            URI[] cacheFiles = context.getCacheFiles();
            // 获取”小表“的路径
            URI uri = cacheFiles[0];
            // 根据uri读取小表的数据
            FileSystem fs = FileSystem.get(context.getConfiguration());
            FSDataInputStream open = fs.open(new Path(uri.toString()));
            BufferedReader br = new BufferedReader(new InputStreamReader(open));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\t")) {
                    String[] sumScoreSplit = line.split("\t");
                    String id = sumScoreSplit[0];
                    String sumScore = sumScoreSplit[1];
                    // 以id作为key，总分sumScore作为value 构建HashTable
                    sumScoreHTable.put(id, sumScore);
                }
            }
        }

        @Override
        // 处理”大表“的数据
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            String[] stuSplits = value.toString().split(",");
            String id = stuSplits[0];
            String name = stuSplits[1];
            String clazz = stuSplits[4];
            // 根据学生id从HashTable中获取学生总分
            String sumScore = sumScoreHTable.getOrDefault(id, "0");

            // 将id、name、clazz、sumScore拼接并直接由Map端输出到HDFS
            String outKey = id + "," + name + "," + clazz + "," + sumScore;
            context.write(new Text(outKey), NullWritable.get());

        }
    }

    // Driver端
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJobName("Demo9MapJoin");
        job.setJarByClass(Demo9MapJoin.class);

        // 配置Map端
        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // 配置输入输出路径
        // 将 ”大表“的数据作为输入路径，小表不需要作为输入路径，待会可以广播
        FileInputFormat.addInputPath(job, new Path("/student/input"));

        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/student/mapjoin/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }
        FileOutputFormat.setOutputPath(job, outPath);

        // 将”小表“的数据 添加到MR的CacheFile中
        job.addCacheFile(new URI("hdfs://cluster/student/score/output/part-r-00000"));

        // 将任务提交并等待完成
        job.waitForCompletion(true);

        /**
         * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo9MapJoin
         */

    }
}
