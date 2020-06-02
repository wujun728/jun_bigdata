package com.service.data.examples.spark.sql.mongodb

import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.MongoInferSchema
import com.service.data.commons.monitor.Stopwatch
import com.service.data.spark.sql.implicits.SparkSqlImplicit._
import com.service.data.spark.sql.utils.{MongoUtil, SparkSessionUtil}
import org.bson.BsonDocument

/**
  * @author 伍鲜
  *
  *         读取MongoDB形成Dataset/DataFrame
  */
object SparkSqlMongoRead {
  def main(args: Array[String]): Unit = {
    loadFromMongoDB()
  }

  def readFromMongoDB(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    val watch = new Stopwatch

    val rdd = MongoUtil.readFromMongoDB()
    val df = rdd.toDF()

    df.printSchema()
    df.debugShow()

    println(s"完成：${watch}")
  }

  def toBsonDocumentRDD(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    val watch = new Stopwatch

    val rdd = MongoUtil.toBsonDocumentRDD[BsonDocument](ReadConfig(Map(ReadConfig.collectionNameProperty -> "DocTest1", ReadConfig.sampleSizeProperty -> "100"), Some(ReadConfig(spark))))
    val df = rdd.toDF()

    df.printSchema()
    df.debugShow()

    println(s"完成：${watch}")
  }

  def mongoInferSchema(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    val watch = new Stopwatch

    MongoInferSchema(MongoUtil.toBsonDocumentRDD[BsonDocument](ReadConfig(Map(ReadConfig.collectionNameProperty -> "DocTest1", ReadConfig.sampleSizeProperty -> "100"), Some(ReadConfig(spark)))))
      .printTreeString()

    println(s"完成：${watch}")
  }

  def loadFromMongoDB(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    // 读取MongoDB
    val df = MongoUtil.loadFromMongoDB("DocTest")

    df.printSchema()
    df.debugShow()
  }
}
