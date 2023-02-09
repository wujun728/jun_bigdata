package com.george.mapreduce.partition;

import com.george.mapreduce.flow.FlowBean;
import com.george.mapreduce.flow.FlowMapper;
import com.george.mapreduce.flow.FlowReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>Driver</p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/7 13:08
 * @since JDK 1.8
 */
@Component
public class PartitionerDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionerDriver.class);

    /**
     * NameNode路径
     */
    @Value("${hadoop.name-node}")
    private String hdfsUrl;

    /**
     * 操作用户
     */
    @Value("${hadoop.username}")
    private String hdfsUser;

    /**
     * 测试自定义分区方法
     * @param inputPath 文件输入路径
     * @param outputPath 结果输出路径
     * @return 操作结果，是否成功
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public int testPartitioner(String inputPath, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        // 远程调试，设置用户名和NameNode
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://hadoop02:9000");

        // 1、获取job实例
        Job job = Job.getInstance(configuration);

        // 2、设置类路径
        job.setJarByClass(PartitionerDriver.class);

        // 3、设置Map和Reduce
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReduce.class);

        // 设置reduceTask数量
        job.setNumReduceTasks(5);
        // 设置自定义分区类
        job.setPartitionerClass(MyPartitioner.class);

        // 4、设置输入和输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 5、设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 6、提交job
        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();
        LOGGER.info("job执行结果 ===> {}", successful);
        return successful ? 0 : 1;
    }
}
