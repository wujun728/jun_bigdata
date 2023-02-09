package com.george.mapreduce.myoutputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/24 23:51
 * @since JDK 1.8
 */
public class MyRecordWriter extends RecordWriter<LongWritable, Text> {

    private FSDataOutputStream atguigu;
    private FSDataOutputStream other;

    /**
     * 初始化方法
     * @param job
     * @throws IOException
     */
    public void initialize(TaskAttemptContext job) throws IOException {
        String outdir = job.getConfiguration().get(FileOutputFormat.OUTDIR);
        FileSystem fileSystem = FileSystem.get(job.getConfiguration());
        atguigu = fileSystem.create(new Path(outdir + "/atguigu.log"));
        other = fileSystem.create(new Path(outdir + "/other.log"));
    }

    /**
     * 将KV写出，每对KV调用一次
     * @param key
     * @param value
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void write(LongWritable key, Text value) throws IOException, InterruptedException {
        String out = value.toString() + "\n";
        if (out.contains("atguigu")) {
            atguigu.write(out.getBytes());
        } else {
            other.write(out.getBytes());
        }
    }

    /**
     * 关闭流
     * @param taskAttemptContext
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeStream(atguigu);
        IOUtils.closeStream(other);
    }
}
