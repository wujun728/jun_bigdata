package com.george.mapreduce.writeablecomparable2;

import com.george.mapreduce.writeablecomparable1.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * <p>
 *     自定义分区
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/8 12:41
 * @since JDK 1.8
 */
public class MyPartitioner2 extends Partitioner<FlowBean, Text> {

    @Override
    public int getPartition(FlowBean flowBean, Text text, int numPartitions) {
        switch (text.toString().substring(0, 3)) {
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
