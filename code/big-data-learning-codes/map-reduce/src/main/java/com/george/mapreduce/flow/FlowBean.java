package com.george.mapreduce.flow;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>
 *     流量统计实体
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/25 10:39
 * @since JDK 1.8
 */
public class FlowBean implements Writable {

    /**
     * 上行流量
     */
    private long upFlow;
    /**
     * 下行流量
     */
    private long downFlow;
    /**
     * 总流量
     */
    private long sumFlow;

    /**
     * 无参构造器
     */
    public FlowBean() {
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }

    /**
     * 自定义set方法，用于设置值
     * @param upFlow
     * @param downFlow
     */
    public void set(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    /**
     * 序列化方
     * @param out 框架提供的数据出口
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    /**
     * 反序列化方法(顺序与序列化方法一致)
     * @param in 框架提供的数据日寇
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        upFlow = in.readLong();
        downFlow = in.readLong();
        sumFlow = in.readLong();
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
}
