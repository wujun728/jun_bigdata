package com.george.mapreduce.myinputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * <p>
 *     自定义FileInputFormat
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/30 19:11
 * @since JDK 1.8
 */
public class MyFileInputFormat extends FileInputFormat<Text, BytesWritable> {
    /**
     * 重写 isSplitable 方法，返回false，文件不可分割
     * @param context
     * @param filename
     * @return
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    /**
     * 重写 createRecordReader，创建自定义的 createRecordReader 对象
     * @param inputSplit
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordReader<Text, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MyRecordReader();
    }
}
