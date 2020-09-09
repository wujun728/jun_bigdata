package com.service.data.examples.spark.streaming.mongodb

import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql.{MongoMapFunctions, MongoSchemaInfer}
import com.service.data.commons.logs.Logging
import com.service.data.spark.sql.utils.{RedisUtil, SparkSessionUtil}
import com.service.data.spark.streaming.receiver.MongoDocumentReceiver
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.bson.Document
import com.service.data.spark.sql.implicits.SparkSqlImplicit._

/**
  * @author 伍鲜
  *
  *         以流/Streaming的方式读取并处理MongoDB的数据
  */
object SparkStreamingMongoRead extends Logging {

  def main(args: Array[String]): Unit = {
    // 一些参数
    val batchSize = 10000
    val commitSize = 10000
    val batchTime = 5

    val readCountKey = "mongodb_receiver_read_count"
    val readCompleteKey = "mongodb_receiver_read_complete"

    implicit val spark: SparkSession = SparkSessionUtil.getSparkSession("SparkStreamingMongoRead")

    val jedis = RedisUtil.jedisClient
    jedis.del(readCountKey)
    jedis.del(readCompleteKey)

    // 累加器，统计Receiver获取的记录数，没读取一条数据增加1
    // val readCount = spark.sparkContext.longAccumulator("ReadCount")
    // 累加器，统计完成的Receiver数量，当Receiver读取完成数据后增加1
    // val readComplete = spark.sparkContext.longAccumulator("ReadComplete")

    val ssc = new StreamingContext(spark.sparkContext, Seconds(batchTime))

    val readConfig = ReadConfig(spark)

    // 计数器，统计已处理的记录数
    var total: Long = 0

    // 从自定义Receiver获取流式数据
    val stream = ssc.receiverStream(new MongoDocumentReceiver(readConfig, new Document(), new Document(), new Document("_id", 1), batchSize, commitSize, batchTime * 1000))

    stream.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        // 推断RDD的Schema
        val schema = MongoSchemaInfer(rdd, readConfig)
        // 根据rdd和schema创建DataFrame
        val df = spark.createDataFrame(rdd.map(MongoMapFunctions.documentToRow(_, schema, Array.empty[String])), schema)
        // 显示数据
        df.debugShow(10)
        // 统计已处理的记录数
        total += df.count()
      }
    })

    ssc.start()

    // 超时时间
    val timeout = batchTime * 1000

    // 如果没有获取到数据源完成
    while (!jedis.exists(readCompleteKey)) {
      // 等待结束或等待超时
      ssc.awaitTerminationOrTimeout(timeout)
    }

    // 等待Streaming程序完成
    while (!ssc.awaitTerminationOrTimeout(timeout)) {
      // 如果没有读取到数据，或者读取到的数据已经处理完成
      if (!jedis.exists(readCountKey) || jedis.get(readCountKey).toLong == total) {
        debug(s"共处理记录：${jedis.get(readCountKey).toLong}")
        // Streaming程序安全退出
        ssc.stop(true, true)
      }
    }
  }
}
