package com.service.data.spark.sql.utils

import com.service.data.commons.property.ServiceProperty
import org.apache.spark.sql.SparkSession
import com.service.data.spark.sql.implicits.SparkSessionImplicit._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author 伍鲜
  *
  *         SparkSession工具
  */
object SparkSessionUtil {

  /**
    * 获取SparkSession
    *
    * @return
    */
  def getSparkSession(): SparkSession = getSparkSession("SparkSQLApplication")

  /**
    * 获取SparkSession
    *
    * @return
    */
  def getSparkSession(appName: String): SparkSession = {

    // 保障在SparkSession初始化前SparkEnv可用
    SparkContext.getOrCreate(new SparkConf().setAppName(appName).setMaster("local[*]"))

    val builder = SparkSession.builder()
      .appName(appName)
      .master("local[*]")

    // MongoDB 参数设置
    ServiceProperty.properties.filter(_._1.startsWith("spark.mongodb")).foreach(x => {
      builder.config(x._1, x._2)
    })

    // Redis 参数设置
    ServiceProperty.properties.filter(_._1.startsWith("spark.redis")).foreach(x => {
      builder.config(x._1, x._2)
    })

    val spark = builder.getOrCreate()

    spark.withDefaultUdfs()

    spark
  }
}
