package com.george.mapreduce.mapjoin;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/12/16 21:38
 * @since JDK 1.8
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private Text k = new Text();
    // 缓存产品数据
    private Map<String, String> map = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // 获取缓存的文件
        URI[] cacheFiles = context.getCacheFiles();
        // 获取缓存文件的路径
        String path = cacheFiles[0].getPath().toString();
        // 通过本地流的形式读取文件
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String line;
        while (StringUtils.isNotEmpty(line = reader.readLine())) {
            // 数据切割
            String[] split = line.split("\t");
            // 缓存数据到map中
            map.put(split[0], split[1]);
        }
        // 关闭流
        IOUtils.closeStream(reader);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        String pname = map.get(fields[1]);
        if (pname == null) {
            pname = "NULL";
        }
        k.set(fields[0] + "\t" + pname + "\t" + fields[2]);
        context.write(k, NullWritable.get());
    }
}
