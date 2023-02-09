package com.george.mapreduce.writeablecomparable2;

import com.george.mapreduce.writeablecomparable1.FlowBean;
import com.george.mapreduce.writeablecomparable1.SortMapper;
import com.george.mapreduce.writeablecomparable1.SortReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     分区排序
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/8 12:43
 * @since JDK 1.8
 */
public class SortDriver2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(SortDriver2.class);
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(SortDriver2.class);
        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);

        job.setPartitionerClass(MyPartitioner2.class);
        job.setNumReduceTasks(5);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(FlowBean.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path("D:/test/input"));
        FileOutputFormat.setOutputPath(job, new Path("D:/test/output"));

        job.waitForCompletion(true);
        boolean successful = job.isSuccessful();

        LOGGER.info("执行结果 ===> {}", successful);
    }
}
