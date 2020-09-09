package com.service.data.spark.sql.implicits

import com.mongodb.spark.sql.MongoUDF
import com.service.data.spark.sql.udfs.UDFs
import org.apache.spark.sql.SparkSession

object SparkSessionImplicit {

  /**
    * SparkSession功能扩展
    *
    * @param spark
    */
  implicit class SparkSessionServiceImplicit(spark: SparkSession) {
    /**
      * 注册自定义函数
      */
    def withDefaultUdfs(): SparkSession = {
      // 分布式ID生成
      spark.sqlContext.udf.register("GenerateID", UDFs.GenerateID _)
      // round
      spark.sqlContext.udf.register("roundDecimal", UDFs.roundDecimal _)
      // bround
      spark.sqlContext.udf.register("broundDecimal", UDFs.broundDecimal _)

      spark
    }
  }

  /**
    * SparkSession功能扩展
    *
    * @param spark
    */
  implicit class SparkSessionMongoImplicit(spark: SparkSession) {
    /**
      * 注册自定义函数
      */
    def withMongoUdfs(): SparkSession = {
      MongoUDF.registerFunctions(spark)
      spark
    }
  }

}
