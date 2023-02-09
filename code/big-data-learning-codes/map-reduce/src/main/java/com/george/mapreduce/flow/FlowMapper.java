package com.george.mapreduce.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

/**
 * <p>
 *     流量统计mapper处理类
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/25 10:49
 * @since JDK 1.8
 */
public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    private Text phone = new Text();
    private FlowBean flow = new FlowBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 获取每一行的内容，根据 \t 分割，得到字符串数组
        String[] fields = value.toString().split("\t");
        // 设置key
        phone.set(fields[1]);
        // 上行流量
        long upFlow = Long.parseLong(fields[fields.length - 3]);
        // 下行流量
        long downFlow = Long.parseLong(fields[fields.length - 2]);
        // 设置总流量
        flow.set(upFlow, downFlow);
        context.write(phone, flow);
    }
}
