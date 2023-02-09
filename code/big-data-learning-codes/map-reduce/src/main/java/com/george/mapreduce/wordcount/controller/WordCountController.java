package com.george.mapreduce.wordcount.controller;

import com.george.mapreduce.wordcount.util.WcDriverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2020/10/17 10:58
 * @since JDK 1.8
 */
@RestController
public class WordCountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountController.class);

    private final WcDriverUtil wcDriverUtil;

    @Autowired
    public WordCountController(WcDriverUtil wcDriverUtil) {
        this.wcDriverUtil = wcDriverUtil;
    }

    /**
     * 单词计数 demo
     * @param inputPath 输入的文件路径
     * @param outputPath 输出的文件夹路径
     * @return
     */
    @GetMapping("/count")
    public String count(@RequestParam(value = "inputPath", required = false, defaultValue = "")String inputPath,
                        @RequestParam(value = "outputPath", required = false, defaultValue = "") String outputPath) {
        if(inputPath.equals("") || outputPath.equals("")) {
            return "参数异常";
        }
        String[] args = new String[2];
        args[0] = inputPath;
        args[1] = outputPath;
        int run = 1;
        try {
            run = wcDriverUtil.wordCount(inputPath, outputPath);
        } catch (Exception e) {
            LOGGER.error("{}", e);
            e.printStackTrace();
        }
        return run > 0 ? "失败" : "成功";
    }
}
