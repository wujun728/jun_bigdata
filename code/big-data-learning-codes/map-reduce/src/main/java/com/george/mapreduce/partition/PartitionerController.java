package com.george.mapreduce.partition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/11/7 13:46
 * @since JDK 1.8
 */
@RestController
public class PartitionerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionerController.class);

    @Autowired
    private PartitionerDriver partitionerDriver;

    /**
     * 自定义分区
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     * @return
     */
    @GetMapping("/partition")
    public String partition(@RequestParam String inputPath, @RequestParam String outputPath) {
        int result = 0;
        try {
            result = partitionerDriver.testPartitioner(inputPath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result > 0 ? "失败" : "成功";
    }
}
