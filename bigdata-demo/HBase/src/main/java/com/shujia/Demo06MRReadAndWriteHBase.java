package com.shujia;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

// 读取stu表，统计性别人数，并将结果写回HBase的 stu_gender_cnt
public class Demo06MRReadAndWriteHBase {
    // Map
    public static class MRReadHBase extends TableMapper<Text, IntWritable> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            String rowkey = Bytes.toString(key.get());

            String gender = Bytes.toString(value.getValue("info".getBytes(), "gender".getBytes()));

            // 以班级作为KeyOut，1 作为ValueOut
            context.write(new Text(gender), new IntWritable(1));
        }
    }

    // Reduce
    // create 'stu_gender_cnt','info'
    public static class MRWriteHBase extends TableReducer<Text, IntWritable, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, NullWritable, Mutation>.Context context) throws IOException, InterruptedException {
            int cnt = 0;
            for (IntWritable value : values) {
                cnt += value.get();
            }

            Put put = new Put(key.getBytes());
            put.addColumn("info".getBytes(), "cnt".getBytes(), (cnt + "").getBytes());

            context.write(NullWritable.get(), put);
        }
    }

    // Driver

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "master:2181,node1:2181,node2:2181");
        conf.set("fs.defaultFS", "hdfs://master:9000");

        Job job = Job.getInstance(conf);

        job.setJobName("Demo06MRReadAndWriteHBase");
        job.setJarByClass(Demo06MRReadAndWriteHBase.class);

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
        TableMapReduceUtil.initTableReducerJob(
                "stu_gender_cnt",
                MRWriteHBase.class,
                job
        );


        job.waitForCompletion(true);
        /**
         * 先创建stu_gender_cnt表
         * create 'stu_gender_cnt','info'
         * 使用Maven插件将依赖打入Jar包中
         * hadoop jar HBase-1.0-jar-with-dependencies.jar com.shujia.Demo06MRReadAndWriteHBase
         */

    }
}
