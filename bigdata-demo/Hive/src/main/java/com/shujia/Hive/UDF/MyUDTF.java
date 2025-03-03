package com.shujia.Hive.UDF;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;

public class MyUDTF extends GenericUDTF {

    @Override
    // initialize方法，会在UDTF被调用的时候执行一次
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        fieldNames.add("col1");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("col2");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,
                fieldOIs);
    }

    @Override
    public void process(Object[] args) throws HiveException {

        // "key1:value1,key2:value2,key3:value3"
        for (Object arg : args) {
            String[] kvSplit = arg.toString().split(",");
            for (String kv : kvSplit) {
                String[] splits = kv.split(":");
                String key = splits[0];
                String value = splits[1];
                ArrayList<String> kvList = new ArrayList<>();
                kvList.add(key);
                kvList.add(value);
                forward(kvList);
            }
        }


    }

    @Override
    public void close() throws HiveException {

    }
}
