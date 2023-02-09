package com.george.hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

/**
 * <p>
 *     Hive自定义UDF函数
 *     新Api实现方式
 * </p>
 *
 * @author George Chan
 * @version 1.0
 * @date 2021/3/7 15:02
 * @since JDK 1.8
 */
public class MyUDF extends GenericUDF { // 新Api，自定义方法，返回字符串长度

    /**
     * 初始化方法，对输入参数做检查，并返回输出参数的的类型
     * @param objectInspectors 输入参数检查器
     * @return 输出参数检查器
     * @throws UDFArgumentException
     */
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        // 判断参数的个数是不是1，不是就抛出异常
        if (objectInspectors.length != 1) {
            throw new UDFArgumentException("Incorrect argument counts");
        }
        // 判断参数的类型是不是基本数据类型，不是就抛出异常（String属于基本数据类型）
        if (!objectInspectors[0].getCategory().equals(ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentTypeException(0, "Incorrect argument type");
        }
        return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    }

    /**
     * 真正实现业务逻辑的方法
     * @param deferredObjects 传入参数
     * @return
     * @throws HiveException
     */
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        Object obj = deferredObjects[0].get();
        if (null == obj) {
            return 0;
        }
        return obj.toString().length();
    }

    /**
     * 当方法执行是出现异常，默认显示的方法内容
     * @param strings
     * @return
     */
    public String getDisplayString(String[] strings) {
        return "";
    }
}
