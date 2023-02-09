package com.george.mapreduce.reducejoin1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/9 20:50
 * @since JDK 1.8
 */
public class TableDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、获取配置信息和Job实例
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        // 2、指定本程序jar包所在的本地路径
        job.setJarByClass(TableDriver.class);
        // 3、指定本业务job所使用的Mapper/Reducer业务类
        job.setMapperClass(TableMapper.class);
        job.setReducerClass(TableReducer.class);
        // 4、指定本程序Mapper输出的KV类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TableBean.class);
        // 5、指定本程序Reduce输出的KV类型
        job.setOutputKeyClass(TableBean.class);
        job.setOutputValueClass(NullWritable.class);
        // 6、指定job输入输出文件目录
        FileInputFormat.setInputPaths(job, new Path("D:/test/hadoop/reducejoin1/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/hadoop/reducejoin1/output"));
        // 7、任务提交
        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();
        System.out.println("任务执行结果：" + successful);
    }
}
