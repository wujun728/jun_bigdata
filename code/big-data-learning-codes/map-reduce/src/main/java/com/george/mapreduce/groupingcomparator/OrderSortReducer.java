package com.george.mapreduce.groupingcomparator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * <p>
 *     Reducer
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/10 14:13
 * @since JDK 1.8
 */
public class OrderSortReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
