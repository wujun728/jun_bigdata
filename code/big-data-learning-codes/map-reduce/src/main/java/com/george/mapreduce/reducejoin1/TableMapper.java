package com.george.mapreduce.reducejoin1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/2 20:22
 * @since JDK 1.8
 */
public class TableMapper extends Mapper<LongWritable, Text, Text, TableBean> {

    private String fileName;
    TableBean bean = new TableBean();
    Text k = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // 1、获取文件切片
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        // 2、根据文件切片获取文件的名称
        fileName = fileSplit.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1、获取数据
        String line = value.toString();
        // 2、对不同文件分别进行处理
        if (fileName.contains("order")) { // 订单数据
            // 切割数据
            String[] fields = line.split("\t");
            // 封装对象
            bean.setOrderId(fields[0]);
            bean.setProductId(fields[1]);
            bean.setAmount(Integer.parseInt(fields[2]));
            bean.setProductName("");
            bean.setFlag("order");
            k.set(bean.getProductId());
        } else { // 产品数据
            String[] fields = line.split("\t");
            bean.setProductId(fields[0]);
            bean.setProductName(fields[1]);
            bean.setFlag("product");
            bean.setAmount(0);
            bean.setOrderId("");
            k.set(bean.getProductId());
        }
        // 3、数据写出
        context.write(k, bean);
    }
}
