package com.george.mapreduce.reducejoin1;

import com.google.common.collect.Lists;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.beans.BeanUtils;
import java.io.IOException;
import java.util.List;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/2 20:42
 * @since JDK 1.8
 */
public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {

    // 订单数据集合
    List<TableBean> orderBeans = Lists.newArrayList();
    // 产品bean对象
    TableBean pdBean = new TableBean();

    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {
        for (TableBean bean : values) {
            if ("order".equals(bean.getFlag())) { // 如果是订单表数据
                TableBean tableBean = new TableBean();
                BeanUtils.copyProperties(bean, tableBean);
                orderBeans.add(tableBean);
            } else { // 产品表数据
                BeanUtils.copyProperties(bean, pdBean);
            }
        }
        // 数据拼接
        for (TableBean bean : orderBeans) {
            bean.setProductName(pdBean.getProductName());
            context.write(bean, NullWritable.get());
        }
        orderBeans.clear();
    }
}
