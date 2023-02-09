package com.george.hdfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/2 20:05
 * @since JDK 1.8
 */
@SpringBootApplication
public class HdfsApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HdfsApplication.class, args);
        LOGGER.info("HdfsApplication启动成功！");
    }
}
