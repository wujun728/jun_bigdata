package com.service.data.examples.spark.sql

import com.service.data.spark.sql.utils.SparkSessionUtil

/**
  * @author 伍鲜
  *
  *         读取文本文件，验证下缓存和非缓存的区别，非缓存情况下，RDD会从头计算
  */
object SparkSqlReadTextFile {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()
    import spark.implicits._

    val rdd1 = spark.read.text("file:///E:/Gitee/DataService-Framework/LICENSE").toJSON.map(x => {
      println("这里计算一遍")
      x
    }).rdd

    rdd1.cache().setName("cache_name")

    val rdd2 = rdd1.filter(_.length > 30)
    val rdd3 = rdd1.map(x => "rdd转换的")

    val ds1 = spark.createDataset(rdd1)

    // 在Action算子前unpersist，那么前面的cache相当于没做
    // rdd1.unpersist()

    println("============================================================rdd1")
    rdd1.foreach(println)
    println("============================================================rdd2")
    rdd2.foreach(println)
    println("============================================================rdd3")
    rdd3.foreach(println)
    println("============================================================ds1")
    ds1.show()

    // 根据名称进行释放
    spark.sparkContext.getPersistentRDDs.filter(_._2.name == "cache_name").foreach(_._2.unpersist())
  }
}
