package com.george.mapreduce.writeablecomparable1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/8 10:52
 * @since JDK 1.8
 */
public class SortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
    private FlowBean flowBean = new FlowBean();
    private Text phone = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        // 设置key
        phone.set(fields[1]);
        // 上行流量
        long upFlow = Long.parseLong(fields[fields.length - 3]);
        flowBean.setUpFlow(upFlow);
        // 下行流量
        long downFlow = Long.parseLong(fields[fields.length - 2]);
        flowBean.setDownFlow(downFlow);
        // 总流量
        flowBean.set(upFlow, downFlow);
        // 输出，bean作为key， 手机号作为value
        context.write(flowBean, phone);
    }
}
