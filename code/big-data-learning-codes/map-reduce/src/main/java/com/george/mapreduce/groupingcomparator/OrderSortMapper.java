package com.george.mapreduce.groupingcomparator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * <p>
 *     Mapper
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/10 11:24
 * @since JDK 1.8
 */
public class OrderSortMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
    OrderBean orderBean = new OrderBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 获取一行数据，并切分
        String[] fields = value.toString().split("\t");
        orderBean.setOrderId(fields[0]);
        orderBean.setProductId(fields[1]);
        orderBean.setPrice(Double.parseDouble(fields[2]));
        context.write(orderBean, NullWritable.get());
    }
}
