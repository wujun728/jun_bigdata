package com.service.data.examples.spark.sql.redis

import com.service.data.spark.sql.implicits.SparkSqlImplicit._
import com.service.data.spark.sql.utils.{RedisUtil, SparkSessionUtil}

/**
  * @author 伍鲜
  *
  *         读取Redis形成Dataset/DataFrame
  */
object SparkSqlRedisRead {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    // 读取Redis
    val df = RedisUtil.loadFromRedis("users", "name")
    df.printSchema()
    df.debugShow()
  }
}
