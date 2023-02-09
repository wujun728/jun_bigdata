package com.george.mapreduce.writeablecomparable1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/8 11:35
 * @since JDK 1.8
 */
public class SortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
    @Override
    protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // reduce接收到的是一组数据，
        // 由于在map阶段，key为bean，value是手机号，
        // 所以这里会接收到一个bean和一个手机号一一对应
        // 并且在reduce阶段已经排好了序
        for (Text value : values) {
            context.write(value, key);
        }
    }
}
