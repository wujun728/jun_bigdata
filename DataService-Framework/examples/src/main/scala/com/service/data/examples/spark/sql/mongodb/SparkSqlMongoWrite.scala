package com.service.data.examples.spark.sql.mongodb

import com.service.data.spark.sql.utils.{MongoUtil, SparkSessionUtil}
import org.bson.Document

/**
  * @author 伍鲜
  *
  *         读取List形成Dataset/DataFrame，并写入MongoDB
  */
object SparkSqlMongoWrite {
  def main(args: Array[String]): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()
    import spark.implicits._

    val list = List("""{"name":"wuxian","age":31, "sex":"01"}""","""{"name":"wuxian","age":31, "sex":"01"}""", """{"name":"winson","age":31,"sex":"02"}""")
    val df = spark.read.json(spark.createDataset(list))
    df.printSchema()
    df.show()

    df.toJSON.foreach(json => {
      val doc = Document.parse(json)
      println(doc)
    })
    df.createOrReplaceTempView("haha")
    spark.sql("select name,collect_list(struct(age,sex)) from haha group by name").toJSON.foreach(json => {
      println(json)
      val doc = Document.parse(json)
      println(doc)
    })

    // 保存到MongoDB
    MongoUtil.saveToMongoDB("SparkWrite", df)
  }
}
