package com.george.hdfs.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/2 20:56
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/hdfs")
public class HdfsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsController.class);

    @GetMapping("/createDir")
    public void createDir () throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop02:9000"), configuration, "hadoop");
        boolean mkdirs = fs.mkdirs(new Path("/123/test/"));
        LOGGER.info("创建文件夹 {}", mkdirs);
        fs.close();
    }
}
