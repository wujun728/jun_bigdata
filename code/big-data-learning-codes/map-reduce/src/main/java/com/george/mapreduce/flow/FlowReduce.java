package com.george.mapreduce.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * <p>
 *     流量统计 reduce处理类
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/25 11:16
 * @since JDK 1.8
 */
public class FlowReduce extends Reducer<Text, FlowBean, Text, FlowBean> {
    FlowBean flowBean = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long upFlow = 0;
        long downFlow = 0;
        for (FlowBean value : values) {
            upFlow += value.getUpFlow();
            downFlow += value.getDownFlow();
        }
        flowBean.set(upFlow, downFlow);
        context.write(key, flowBean);
    }
}
