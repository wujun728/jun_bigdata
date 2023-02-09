package com.george.mapreduce.wordcount.util;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/17 10:16
 * @since JDK 1.8
 */
public class WcMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    Text k = new Text();
    IntWritable v = new IntWritable(1);

    /**
     * 重写map方法
     * map 方法一次处理一行数据
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 获取一行数据
        String line = value.toString();
        // 将一行数据根据空格进行分割
        String[] words = line.split(" ");
        // 数据输出到上下文对象
        for (String word : words) {
            k.set(word);
            context.write(k, v);
        }
    }
}
