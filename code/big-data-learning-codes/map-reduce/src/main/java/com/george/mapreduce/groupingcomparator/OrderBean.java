package com.george.mapreduce.groupingcomparator;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * <p>
 *     订单实体
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/10 11:05
 * @since JDK 1.8
 */
public class OrderBean implements WritableComparable<OrderBean> {
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 订单价格
     */
    private double price;

    public OrderBean() {
    }

    public OrderBean(String orderId, String productId, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * 自定义toString方法，文件中输出
     * @return
     */
    @Override
    public String toString() {
        return orderId + "\t" + productId + "\t" + price;
    }

    /**
     * 二次比较
     * @param o
     * @return
     */
    @Override
    public int compareTo(OrderBean o) {
        int compare = orderId.compareTo(o.orderId);
        if (compare == 0) {
            return Double.compare(o.price, price);
        } else {
            return compare;
        }
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
        out.writeDouble(price);
    }

    /**
     * 反序列化
     * @param input
     * @throws IOException
     */
    @Override
    public void readFields(DataInput input) throws IOException {
        orderId = input.readUTF();
        productId = input.readUTF();
        price = input.readDouble();
    }
}
