package com.service.data.kafka.clients.producer

import java.util.Properties

import com.service.data.kafka.clients.properties.KafkaProperties
import org.apache.kafka.clients.producer.KafkaProducer

import scala.collection.JavaConversions._

/**
  * @author 伍鲜
  *
  *         简单的Kafka生产者
  */
object SimpleProducer {
  /**
    * Kafka生产者创建
    */
  val producer = {
    val producer = new KafkaProducer[String, String](KafkaProperties.kafkaProducerProperties)
    sys.addShutdownHook({
      producer.close()
    })
    producer
  }

  def apply[K, V](): KafkaProducer[K, V] = apply(KafkaProperties.kafkaProducerProperties)

  def apply[K, V](config: Properties): KafkaProducer[K, V] = apply(config.toMap)

  def apply[K, V](config: Map[String, Object]): KafkaProducer[K, V] = {
    val producer = new KafkaProducer[K, V](config)
    sys.addShutdownHook({
      producer.close()
    })
    producer
  }
}
