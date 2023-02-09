package com.george.mapreduce.etl1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * <p>
 *     数据清洗Mapper
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/16 22:20
 * @since JDK 1.8
 */
public class ETLMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(" ");
        if (fields.length > 11) {
            context.write(value, NullWritable.get());
            context.getCounter("ETL", "True").increment(1);
        } else {
            context.getCounter("ETL", "False").increment(1);
        }
    }
}
