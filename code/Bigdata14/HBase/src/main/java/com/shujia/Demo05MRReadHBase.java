package com.shujia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

// 读取stu表，统计班级人数
public class Demo05MRReadHBase {
    // Map
    public static class MRReadHBase extends TableMapper<Text, IntWritable> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            String rowkey = Bytes.toString(key.get());

            String clazz = Bytes.toString(value.getValue("info".getBytes(), "clazz".getBytes()));

            // 以班级作为KeyOut，1 作为ValueOut
            context.write(new Text(clazz), new IntWritable(1));
        }
    }

    // Reduce
    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int cnt = 0;
            for (IntWritable value : values) {
                cnt += value.get();
            }
            context.write(key, new IntWritable(cnt));
        }
    }

    // Driver

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        conf.set("fs.defaultFS", "hdfs://master:9000");

        Job job = Job.getInstance(conf);

        job.setJobName("Demo05MRReadHBase");
        job.setJarByClass(Demo05MRReadHBase.class);

        // 配置Map任务
        TableMapReduceUtil.initTableMapperJob(
                "stu",
                new Scan(),
                MRReadHBase.class,
                Text.class,
                IntWritable.class,
                job
        );


        // 配置Reduce任务
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 配置输入输出路径
        Path path = new Path("/MR/HBase/output/");
        FileSystem fs = FileSystem.get(conf);

        if (fs.exists(path)) {
            fs.delete(path, true);
        }

        FileOutputFormat.setOutputPath(job, path);


        job.waitForCompletion(true);
        /**
         * 配置Hadoop运行时的依赖环境
         * export HADOOP_CLASSPATH="$HBASE_HOME/lib/*"
         * 提交任务
         * hadoop jar HBase-1.0.jar com.shujia.Demo05MRReadHBase
         */

    }
}
