package com.service.data.spark.streaming.receiver

import com.mongodb.client.MongoCollection
import com.mongodb.spark.MongoConnector
import com.mongodb.spark.config.ReadConfig
import com.service.data.commons.logs.Logging
import com.service.data.spark.sql.utils.RedisUtil
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.bson.{BsonDocument, Document}

/**
  * @author 伍鲜
  *
  *         自定义Receiver，用来产生流式数据
  * @param readConfig
  * @param find
  * @param projection
  * @param sort
  * @param batchSize
  * @param commitSize
  * @param sleepTime
  */
class MongoDocumentReceiver(readConfig: ReadConfig,
                            find: Document, // 查询器
                            projection: Document, // 筛选器
                            sort: Document, // 排序
                            batchSize: Int = 1024,
                            commitSize: Int = 1024,
                            sleepTime: Long = 5000)
  extends Receiver[BsonDocument](StorageLevel.MEMORY_AND_DISK) with Logging {
  private val readCountKey = "mongodb_receiver_read_count"
  private val readCompleteKey = "mongodb_receiver_read_complete"

  override def onStart(): Unit = {
    val jedis = RedisUtil.jedisClient

    info("启动自定义Receiver")
    MongoConnector(readConfig).withCollectionDo(readConfig, { collection: MongoCollection[BsonDocument] =>
      info("Receiver读取数据[启动]")
      val batch = collection.find(find).projection(projection).sort(sort).batchSize(batchSize).iterator()
      while (batch.hasNext()) {
        store(batch.next())
        val count = jedis.incr(readCountKey)
        if (count % commitSize == 0) {
          // 线程等待
          // 目的是预防数据生成过快，SparkStreaming来不及处理
          // 等待时间与SparkStreaming的微批时间间隔保持一致
          // 尽量保障同一个微批时间间隔内，产生多少数据，SparkStreaming就有能力消费多少数据
          Thread.sleep(sleepTime)
        }
      }
      info("Receiver读取数据[完成]")
      jedis.incr(readCompleteKey)
      info(s"Receiver读取记录：${jedis.get(readCountKey)}")
    })
  }

  override def onStop(): Unit = {
    info("停止自定义Receiver")
  }
}
