package com.mongodb.spark.sql

import com.mongodb.spark.sql.helpers.UDF
import org.apache.spark.sql.SparkSession

object MongoUDF {

  /**
    * 注册自定义函数
    */
  def registerFunctions(spark: SparkSession): Unit = {
    UDF.registerFunctions(spark)
  }
}
