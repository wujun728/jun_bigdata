package com.george.mapreduce.myoutputformat;

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
 * @date 2020/11/25 0:00
 * @since JDK 1.8
 */
public class MyOutputFormat extends FileOutputFormat<LongWritable, Text> {
    @Override
    public RecordWriter<LongWritable, Text> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        MyRecordWriter recordWriter = new MyRecordWriter();
        recordWriter.initialize(job);
        return recordWriter;
    }
}
