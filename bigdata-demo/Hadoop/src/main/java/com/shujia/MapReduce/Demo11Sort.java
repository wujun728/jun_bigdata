package com.shujia.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Demo11Sort {
    // Map端
    public static class MyMapper extends Mapper<LongWritable, Text, KeySort, Text> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, KeySort, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            String id = split[0];
            String name = split[1];
            String clazz = split[2];
            String score = split[3];
            KeySort mapOutKey = new KeySort(clazz, Integer.valueOf(score));
            context.write(mapOutKey, new Text(id + "," + name));
        }
    }

    // Reduce端
    public static class MyReducer extends Reducer<KeySort, Text, Text, Text> {
        @Override
        protected void reduce(KeySort key, Iterable<Text> values, Reducer<KeySort, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(new Text(key.clazz + "," + key.score), value);
            }
        }
    }

    // Driver端
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("Demo11Sort");
        job.setJarByClass(Demo11Sort.class);

        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(KeySort.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 配置输入输出路径
        /**
         * hdfs dfs -mkdir -p /student/sort/input
         * hdfs dfs -put stuSumScore.txt.txt /student/sort/input
         */

        FileInputFormat.addInputPath(job, new Path("/student/sort/input"));
        // 输出路径不需要提前创建，如果该目录已存在则会报错
        // 通过HDFS的JavaAPI判断输出路径是否存在
        Path outPath = new Path("/student/sort/output");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outPath)) {
            fs.delete(outPath, true);
        }

        FileOutputFormat.setOutputPath(job, outPath);

        // 等待job运行完成
        job.waitForCompletion(true);
    }
    /**
     * hadoop jar Hadoop-1.0.jar com.shujia.MapReduce.Demo11Sort
     */
}

/**
 * 自定义排序：实现WritableComparable接口
 */
class KeySort implements WritableComparable<KeySort> {
    String clazz;
    Integer score;

    public KeySort() {

    }

    public KeySort(String clazz, Integer score) {
        this.clazz = clazz;
        this.score = score;
    }

    @Override
    // 自定义排序规则
    public int compareTo(KeySort o) {
        // 首先按班级排序，然后在班级中按value排序
        int clazzCompare = this.clazz.compareTo(o.clazz);
        if (clazzCompare < 0) {
            return -1;
        } else if (clazzCompare > 0) {
            return 1;
        } else {
            int scoreCompare = this.score - o.score;
            if (scoreCompare < 0) {
                return 1;
            } else if (scoreCompare > 0) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(clazz);
        out.writeInt(score);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        clazz = in.readUTF();
        score = in.readInt();

    }
}

