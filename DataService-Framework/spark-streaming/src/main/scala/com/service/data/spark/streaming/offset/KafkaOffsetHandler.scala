package com.service.data.spark.streaming.offset

import java.util.ServiceLoader

import com.service.data.commons.property.ServiceProperty
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

/**
  * @author 伍鲜
  *
  *         Kafka偏移量管理
  */
trait KafkaOffsetHandler {

  /**
    * 读取偏移量
    *
    * @param group
    * @param topic
    * @return
    */
  def readOffset(group: String, topic: String): Map[TopicPartition, Long]

  /**
    * 读取偏移量
    *
    * @param group
    * @param topics
    * @return
    */
  def readOffset(group: String, topics: Seq[String]): Map[TopicPartition, Long] = {
    var fromOffsets: Map[TopicPartition, Long] = Map()

    topics.foreach(topic => {
      fromOffsets ++= readOffset(group, topic)
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
  def writeOffset(group: String, topic: String, offsetRanges: Array[OffsetRange]): Unit

  /**
    * 保存偏移量
    *
    * @param group
    * @param topics
    * @param offsetRanges
    */
  def writeOffset(group: String, topics: Seq[String], offsetRanges: Array[OffsetRange]): Unit = {
    topics.foreach(topic => {
      writeOffset(group, topic, offsetRanges.filter(_.topic == topic))
    })
  }
}

/**
  * @author 伍鲜
  *
  *         Kafka偏移量管理工厂类
  */
object KafkaOffsetHandlerFactory {

  /**
    * 获取Kafka偏移量管理具体实现
    *
    * @return
    */
  def getKafkaOffsetHandler(): KafkaOffsetHandler = {
    val sl = ServiceLoader.load(classOf[KafkaOffsetHandler])
    val iterator = sl.iterator()
    val key = "kafka.offset.handler.class"
    val className = ServiceProperty.properties.getOrElse(key, "")
    if (className == "") {
      throw new IllegalArgumentException(s"Not found properties: ${key}")
    }
    var instance: KafkaOffsetHandler = null
    while (iterator.hasNext) {
      val obj = iterator.next()
      if (obj.getClass.getName == className) {
        instance = obj
      }
    }
    if (instance == null) {
      throw new NoClassDefFoundError(s"Not found class: ${className}")
    } else {
      instance
    }
  }
}