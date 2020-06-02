package com.service.data.examples.spark.sql.mongodb

import com.service.data.commons.logs.Logging
import com.service.data.spark.sql.implicits.SparkSqlImplicit._
import com.service.data.spark.sql.paging.{SparkMongoPagingByJava, SparkMongoPagingBySpark, SparkMongoPagingByView}
import com.service.data.spark.sql.utils.SparkSessionUtil
import org.apache.spark.sql.DataFrame
import org.bson.Document

import scala.util.Try

/**
  * @author 伍鲜
  *
  *         分页读取MongoDB形成Dataset/DataFrame
  */
object SparkSqlMongoReadPage extends Logging {
  def main(args: Array[String]): Unit = {
    mongoPageReadByView()
//    mongoPageReadByJava()
//    mongoPageReadBySpark()
  }

  def mongoPageReadByView(): Unit = {
    debug("采用创建临时View的方式进行分页")
    implicit val spark = SparkSessionUtil.getSparkSession()

    val paging = new SparkMongoPagingByView(spark)

    paging.pageSize = 10000
    paging.queryDocument = Document.parse("""{ $nor : [{ name : "伍鲜1707520481" }]}""")
    // paging.queryDocument = Document.parse("""{ $or : [{ name : "伍鲜1707520481" }, { name:"伍鲜632003428"}]}""")
    paging.projectFields = Seq("name", "age")

    var df: DataFrame = null
    var total: Long = 0

    while ( {
      df = paging.nextPage()
      Try(df.head()).isSuccess
    }) {
      df.printSchema()
      df.debugShow()
      val count = df.count()
      total += count
      println(s"当前处理${count}，最大ID为${paging.minimumObjectId}，总共处理${total}")
    }
  }

  @deprecated
  def mongoPageReadByJava(): Unit = {
    debug("采用JavaAPI的方式进行分页")
    implicit val spark = SparkSessionUtil.getSparkSession()

    val paging = new SparkMongoPagingByJava(spark)

    paging.pageSize = 10000
    paging.queryDocument = Document.parse("""{ $nor : [{ name : "伍鲜1707520481" }]}""")
    // paging.queryDocument = Document.parse("""{ $or : [{ name : "伍鲜1707520481" }, { name:"伍鲜632003428"}]}""")
    paging.projectFields = Seq("name", "age")

    var total: Long = 0
    while (paging.hasNextPage()) {
      val df = paging.nextPage()
      df.printSchema()
      df.debugShow()
      val count = df.count()
      total += count
      println(s"当前处理${count}，最大ID为${paging.minimumObjectId}，总共处理${total}")
    }
  }

  @deprecated
  def mongoPageReadBySpark(): Unit = {
    debug("采用MongoSpark的方式进行分页")
    implicit val spark = SparkSessionUtil.getSparkSession()

    val paging = new SparkMongoPagingBySpark(spark)

    paging.pageSize = 10000
    paging.queryDocument = Document.parse("""{ $nor : [{ name : "伍鲜1707520481" }]}""")
    // paging.queryDocument = Document.parse("""{ $or : [{ name : "伍鲜1707520481" }, { name:"伍鲜632003428"}]}""")
    paging.projectFields = Seq("name", "age")

    var total: Long = 0
    while (paging.hasNextPage()) {
      val df = paging.nextPage()
      df.printSchema()
      df.debugShow()
      val count = df.count()
      total += count
      println(s"当前处理${count}，最大ID为${paging.minimumObjectId}，总共处理${total}")
    }
  }
}
