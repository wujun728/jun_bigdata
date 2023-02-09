package com.george.mapreduce.myinputformat;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import java.io.IOException;

/**
 * <p>
 *     自定义RecordReader，处理一个文件；把这个文件直接读成一个KV值
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/30 19:14
 * @since JDK 1.8
 */
public class MyRecordReader extends RecordReader<Text, BytesWritable> {
    // 文件切片
    private FileSplit split;
    // 文件流对象
    private FSDataInputStream inputStream;
    // 文件是否没有读过，初始值true
    private boolean notRead = true;

    // KV
    private Text key = new Text();
    private BytesWritable value = new BytesWritable();

    /**
     * 初始化，设置配置对象和切片对象
     * @param inputSplit
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext context) throws IOException {
        //转换切片类型到文件切片
        split = (FileSplit) inputSplit;
        //通过切片获取路径
        Path path = split.getPath();
        //通过路径获取文件系统
        FileSystem fileSystem = path.getFileSystem(context.getConfiguration());
        // 开流
        inputStream = fileSystem.open(path);
    }

    /**
     * 读取下一组KV值
     * @return 如果读到了文件，返回true， 读完了返回false
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException {
        if (notRead) {
            //具体读文件的过程
            //读Key
            key.set(split.getPath().toString());
            // 读value
            byte[] bytes = new byte[(int) split.getLength()];
            inputStream.read(bytes);
            value.set(bytes, 0, bytes.length);

            notRead = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前读到的key
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    /**
     * 获取当前读到的value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return notRead ? 0 : 1;
    }

    /**
     * 关闭资源
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        IOUtils.closeStream(inputStream);
    }
}
