package com.george.mapreduce.writeablecomparable1;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/8 10:40
 * @since JDK 1.8
 */
public class FlowBean implements WritableComparable<FlowBean> {
    private long upFlow;
    private long downFlow;
    private long sumFlow;

    public FlowBean() {
    }

    public FlowBean(long upFlow, long downFlow, long sumFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = sumFlow;
    }

    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }

    public void set(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }

    /**
     * 自定义排序方法
     * 按照送流量降序排列
     * @param o
     * @return
     */
    @Override
    public int compareTo(FlowBean o) {
        // 二次排序
//        int compare = Long.compare(o.sumFlow, this.sumFlow);
//        if (compare == 0) {
//            return Long.compare(o.downFlow, this.downFlow);
//        } else {
//            return compare;
//        }
        return Long.compare(o.sumFlow, this.sumFlow);
    }

    /**
     * 序列化方法
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    /**
     * 反序列化方法
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        upFlow = in.readLong();
        downFlow = in.readLong();
        sumFlow = in.readLong();
    }
}
