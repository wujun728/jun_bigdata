package com.george.mapreduce.reducejoin2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/9 23:17
 * @since JDK 1.8
 */
public class OrderBean implements WritableComparable<OrderBean> {
    /**
     * 订单id
     */
    private String id;
    /**
     * 商品id
     */
    private String pid;
    /**
     * 商品数量
     */
    private int amount;
    /**
     * 商品名称
     */
    private String pname;

    @Override
    public String toString() {
        return id + "\t" + pname + "\t" + amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    /**
     * 先按照产品id排序，如果产品id相同则比较产品名称，
     * 目的是为了将产品表的数据分组后放在第一条
     * @param o
     * @return
     */
    @Override
    public int compareTo(OrderBean o) {
        int compare = this.pid.compareTo(o.pid);

        if (compare == 0) {
            return o.pname.compareTo(this.pname);
        } else {
            return compare;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(pid);
        out.writeInt(amount);
        out.writeUTF(pname);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readUTF();
        this.pid = in.readUTF();
        this.amount = in.readInt();
        this.pname = in.readUTF();
    }
}
