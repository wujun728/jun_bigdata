package com.george.mapreduce.reducejoin2;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * <p>
 *     分组排序，将同一个产品id放到一组
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/9 23:47
 * @since JDK 1.8
 */
public class ReduceJoinComparator extends WritableComparator {
    protected ReduceJoinComparator() {
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean oa = (OrderBean) a;
        OrderBean ob = (OrderBean) b;
        return oa.getPid().compareTo(ob.getPid());
    }
}
