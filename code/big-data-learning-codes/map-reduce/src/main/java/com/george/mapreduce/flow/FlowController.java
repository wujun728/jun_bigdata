package com.george.mapreduce.flow;

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
 * @date 2020/10/27 22:10
 * @since JDK 1.8
 */
@RestController
public class FlowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowController.class);

    @Autowired
    private FlowDriver flowDriver;

    @GetMapping("/flowStatistic")
    public String flowStatistic(@RequestParam(value = "inputPath", required = false, defaultValue = "") String inputPath,
                                @RequestParam(value = "outputPath", required = false, defaultValue = "") String outputPath) {
        if (inputPath.equals("") || outputPath.equals("")) {
            return "参数不能为空";
        }
        int result = 1;
        try {
            result = flowDriver.flowStatistics(inputPath, outputPath);
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
