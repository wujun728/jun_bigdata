package com.george.mapreduce.reducejoin1;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/2 20:09
 * @since JDK 1.8
 */
public class TableBean implements Writable {

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 产品数量
     */
    private int amount;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 表的标记
     */
    private String flag;

    public TableBean() {
    }

    public TableBean(String orderId, String productId, int amount, String productName, String flag) {
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
        this.productName = productName;
        this.flag = flag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * 序列化
     * @param out
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderId);
        out.writeUTF(productId);
        out.writeInt(amount);
        out.writeUTF(productName);
        out.writeUTF(flag);
    }

    /**
     * 反序列化
     * @param in
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderId = in.readUTF();
        this.productId = in.readUTF();
        this.amount = in.readInt();
        this.productName = in.readUTF();
        this.flag = in.readUTF();
    }

    @Override
    public String toString() {
        return "orderId=" + orderId +  "\t productId=" + productId + "\t amount=" + amount + "\t productName=" + productName;
    }
}
