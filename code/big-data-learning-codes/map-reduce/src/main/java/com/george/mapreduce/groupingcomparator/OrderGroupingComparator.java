package com.george.mapreduce.groupingcomparator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * <p>
 *     自定义分组排序
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/10 14:19
 * @since JDK 1.8
 */
public class OrderGroupingComparator extends WritableComparator {
    // 创建一个构造函数将比较对象的类传给父类
    public OrderGroupingComparator() {
        super(OrderBean.class, true);
    }

    /**
     * 重写compare方法，为了根据订单号给数据进行分组，这里只比较要分组的列即可（即：订单号）。
     * 这样在reduce端接收到的数据都是一组订单号相同的数据
     * @param a
     * @param b
     * @return
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean o1 = (OrderBean) a;
        OrderBean o2 = (OrderBean) b;
        // 比较订单号，根据订单号进行数据分组
        return o1.getOrderId().compareTo(o2.getOrderId());
    }
}
