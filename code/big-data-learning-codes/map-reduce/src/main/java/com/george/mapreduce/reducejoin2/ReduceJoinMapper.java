package com.george.mapreduce.reducejoin2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/9 23:25
 * @since JDK 1.8
 */
public class ReduceJoinMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
    private OrderBean orderBean = new OrderBean();
    private String fileName;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit)context.getInputSplit();
        fileName = fileSplit.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        if (fileName.contains("order")) {
            orderBean.setId(fields[0]);
            orderBean.setPid(fields[1]);
            orderBean.setAmount(Integer.parseInt(fields[2]));
            orderBean.setPname("");
        } else {
            orderBean.setPid(fields[0]);
            orderBean.setPname(fields[1]);
            orderBean.setId("");
            orderBean.setAmount(0);
        }
        context.write(orderBean, NullWritable.get());
    }
}
