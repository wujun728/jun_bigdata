package com.george.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * <p>
 *     hive自定义UDF函数
 *     旧Api实现方式
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2021/3/7 10:57
 * @since JDK 1.8
 */
public class MyUdfOld extends UDF {
    /**
     * 默认执行的方法
     * 判断字符串的长度
     * @param str 传入参数
     * @return
     */
    public int evaluate(final String str) {
        if (null == str) {
            return 0;
        }
        return str.length();
    }
}
