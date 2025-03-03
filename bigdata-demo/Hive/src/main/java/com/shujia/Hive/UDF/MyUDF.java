package com.shujia.Hive.UDF;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyUDF extends UDF {
    // 自定义UDF 需要继承UDF类，实现evaluate方法
    public String evaluate(String clazz) {
        // 理科三班
        String resStr = "";
        resStr = clazz.replace("一", "1");
        resStr = resStr.replace("二", "2");
        resStr = resStr.replace("三", "3");
        resStr = resStr.replace("四", "4");
        resStr = resStr.replace("五", "5");
        resStr = resStr.replace("六", "6");
        return resStr;

    }

}
