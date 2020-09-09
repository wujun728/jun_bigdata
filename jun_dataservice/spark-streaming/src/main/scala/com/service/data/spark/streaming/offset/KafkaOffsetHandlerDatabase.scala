package com.service.data.spark.streaming.offset

import com.service.data.commons.dbs.DBConnectionPool
import com.service.data.commons.property.ServiceProperty
import com.service.data.commons.tools.IdWorker
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import scalikejdbc._

/**
  * @author 伍鲜
  *
  *         数据库实现Kafka偏移量管理
  */
class KafkaOffsetHandlerDatabase extends KafkaOffsetHandler {
  private val symbol: Symbol = Symbol(ServiceProperty.properties.getOrElse("kafka.offset.handler.database.symbol", "config"))
  private val idWorker: IdWorker = new IdWorker

  /**
    * 读取偏移量
    *
    * @param group
    * @param topic
    * @return
    */
  override def readOffset(group: String, topic: String): Map[TopicPartition, Long] = {
    var fromOffsets: Map[TopicPartition, Long] = Map()

    DBConnectionPool.getNamedDB(symbol) readOnly ({ implicit session =>
      sql"select partition_num, until_offset from app_t_kafka_offset_list where group_id = ${group} and topic_name = ${topic}".map(rs => (rs.int(1), rs.long(2))).list().apply().foreach(record => {
        fromOffsets += (new TopicPartition(topic, record._1) -> record._2)
      })
    })

    fromOffsets
  }

  /**
    * 保存偏移量
    *
    * @param group
    * @param topic
    * @param offsetRanges
    */
  override def writeOffset(group: String, topic: String, offsetRanges: Array[OffsetRange]): Unit = {
    try {
      DBConnectionPool.getNamedDB(symbol) localTx ({ implicit session =>
        offsetRanges.foreach(offsetRange => {
          val count = sql"select count(*) from app_t_kafka_offset_list where group_id = ${group} and topic_name = ${topic} and partition_num = ${offsetRange.partition}".map(rs => rs.int(1)).single().apply().get
          if (count == 0) {
            sql"insert into app_t_kafka_offset_list (group_id,topic_name,partition_num,from_offset,until_offset,row_key) values (${group},${topic},${offsetRange.partition},${offsetRange.fromOffset},${offsetRange.untilOffset},${idWorker.nextId})".update().apply()
          } else {
            sql"update app_t_kafka_offset_list set from_offset = ${offsetRange.fromOffset},until_offset = ${offsetRange.untilOffset} where group_id = ${group} and topic_name = ${topic} and partition_num = ${offsetRange.partition}".update().apply()
          }
        })
      })
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
  }
}
