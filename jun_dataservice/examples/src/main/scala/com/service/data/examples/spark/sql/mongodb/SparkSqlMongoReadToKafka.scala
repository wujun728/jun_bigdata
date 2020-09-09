package com.service.data.examples.spark.sql.mongodb

import com.service.data.kafka.clients.producer.{KafkaSink, SimpleProducer}
import com.service.data.kafka.clients.properties.KafkaProperties
import com.service.data.spark.sql.paging.SparkMongoPagingByView
import com.service.data.spark.sql.utils.SparkSessionUtil
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.sql.DataFrame
import org.apache.spark.{SparkEnv, TaskContext}
import org.bson.Document

import scala.util.Try

/**
  * @author 伍鲜
  *
  *         分页读取MongoDB的数据并写入到Kafka
  */
object SparkSqlMongoReadToKafka {
  def main(args: Array[String]): Unit = {
    writeToKafkaByBroadcastProducer()
    writeToKafkaBySimpleProducer()
  }

  /**
    * 广播变量方式创建Producer
    */
  def writeToKafkaByBroadcastProducer(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    val paging = new SparkMongoPagingByView(spark)

    paging.pageSize = 10000

    paging.queryDocument = Document.parse("""{ $nor : [{ name : "伍鲜1707520481" }]}""")
    // paging.queryDocument = Document.parse("""{ $or : [{ name : "伍鲜1707520481" }, { name:"伍鲜632003428"}]}""")
    paging.projectFields = Seq("name", "age")

    var df: DataFrame = null

    // 初始化广播 Kafka Producer
    val kafkaProducer = {
      spark.sparkContext.broadcast(KafkaSink[String, String](KafkaProperties.kafkaProducerProperties))
    }

    while ( {
      df = paging.nextPage()
      Try(df.head()).isSuccess
    }) {
      val ds = df.toJSON
      ds.foreachPartition(rdd => {
        rdd.foreach(record => {
          kafkaProducer.value.send("BroadcastProducerToKafka", s"${SparkEnv.get.executorId} =====> ${TaskContext.get().partitionId()} =====> ${SimpleProducer.producer} =====> ${record}")
        })
      })
    }
  }

  /**
    * 静态方式创建Producer
    */
  def writeToKafkaBySimpleProducer(): Unit = {
    implicit val spark = SparkSessionUtil.getSparkSession()

    val paging = new SparkMongoPagingByView(spark)

    paging.pageSize = 10000

    paging.queryDocument = Document.parse("""{ $nor : [{ name : "伍鲜1707520481" }]}""")
    // paging.queryDocument = Document.parse("""{ $or : [{ name : "伍鲜1707520481" }, { name:"伍鲜632003428"}]}""")
    paging.projectFields = Seq("name", "age")

    var df: DataFrame = null

    while ( {
      df = paging.nextPage()
      Try(df.head()).isSuccess
    }) {
      val ds = df.toJSON
      ds.foreachPartition(rdd => {
        rdd.foreach(record => {
          SimpleProducer.producer.send(new ProducerRecord[String, String]("SimpleProducerToKafka", s"${SparkEnv.get.executorId} =====> ${TaskContext.get().partitionId()} =====> ${SimpleProducer.producer} =====> ${record}"))
        })
      })
    }
  }
}
