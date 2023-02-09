package com.george.mapreduce.partition;

import com.george.mapreduce.flow.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * <p>
 *     自定义分区
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/7 13:00
 * @since JDK 1.8
 */
public class MyPartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {
        String phone = text.toString();
        String phonePrefix = phone.substring(0, 3);
        switch (phonePrefix) {
            case "136":
                return 0;
            case "137":
                return 1;
            case "138":
                return 2;
            case "139":
                return 3;
            default:
                return 4;
        }
    }
}
