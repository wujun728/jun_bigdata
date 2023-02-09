package com.george.mapreduce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *     MapReduce程序启动主类
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/18 15:18
 * @since JDK 1.8
 */
@SpringBootApplication
public class MapReduceApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapReduceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MapReduceApplication.class, args);
        LOGGER.info("MapReduce程序启动成功！");
    }
}
