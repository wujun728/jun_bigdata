package com.service.data.examples.spark.sql

import com.service.data.commons.monitor.Stopwatch
import com.service.data.spark.sql.utils.SparkSessionUtil

/**
  * @author 伍鲜
  *
  *         读取List形成Dataset/DataFrame
  */
object SparkSqlReadList {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()
    import spark.implicits._

    val watch = new Stopwatch()

    sys.addShutdownHook {
      println(s"执行完成，总共耗时：${watch}")
      println(spark.sparkContext.getConf.getAll)
    }

    val list = List("""{"name":"wuxian","age":31, "sex":"01"}""", """{"name":"winson","age":31,"sex":"02"}""")
    val df = spark.read.json(spark.createDataset(list))
    df.printSchema()
    df.show()

    df.createOrReplaceTempView("users")
    spark.sql("select name, max(age) as max_age from users group by name").createOrReplaceTempView("users_age")

    spark.sql("select t1.name, t2.max_age from users t1 left join users_age t2 on t1.name = t2.name").show()
  }
}
