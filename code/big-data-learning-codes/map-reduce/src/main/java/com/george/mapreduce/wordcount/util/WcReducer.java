package com.george.mapreduce.wordcount.util;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/17 10:18
 * @since JDK 1.8
 */
public class WcReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * 重写reduce方法
     * 一次处理一组数据
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable count : values) {
            sum += count.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
