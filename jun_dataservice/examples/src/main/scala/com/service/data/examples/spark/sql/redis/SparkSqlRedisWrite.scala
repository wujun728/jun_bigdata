package com.service.data.examples.spark.sql.redis

import com.service.data.spark.sql.utils.{RedisUtil, SparkSessionUtil}
import org.apache.spark.sql.SaveMode

/**
  * @author 伍鲜
  *
  *         读取List形成Dataset/DataFrame，并写入Redis
  */
object SparkSqlRedisWrite {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()
    import spark.implicits._

    val list = List("""{"name":"wuxian","age":31, "sex":"01"}""", """{"name":"winson","age":31,"sex":"02"}""")
    val df = spark.read.json(spark.createDataset(list))
    df.printSchema()
    df.show()
    // 保存到Redis
    RedisUtil.writeToRedis(df, "users", "name", SaveMode.Overwrite)
  }
}
