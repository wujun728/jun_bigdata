package com.service.data.examples.spark.sql

import com.service.data.spark.sql.utils.SparkSessionUtil

import com.service.data.spark.sql.implicits.SparkSessionImplicit._

/**
  * @author 伍鲜
  *
  *         Spark SQL 自定义函数
  */
object SparkSqlUdfs {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession().withMongoUdfs()
    import spark.implicits._

    val list = List("""{"name":"wuxian","age":31, "sex":"01"}""", """{"name":"winson","age":31,"sex":"02"}""")
    val df = spark.read.json(spark.createDataset(list))

    df.createOrReplaceTempView("users")
    spark.sql("select name, max(age) as max_age from users group by name").createOrReplaceTempView("users_age")

    spark.sql("select GenerateID(), ObjectId('5d38748723e2061323670264'), t1.name, t2.max_age from users t1 left join users_age t2 on t1.name = t2.name").show()

    spark.sql("select 27138893.600000 * ((100.000000 * 1.000000)/(1.000000*48337.32))").show()
  }
}
