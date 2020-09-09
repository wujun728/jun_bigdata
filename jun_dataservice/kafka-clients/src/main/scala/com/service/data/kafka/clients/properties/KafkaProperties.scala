package com.service.data.kafka.clients.properties

import com.service.data.commons.property.ServiceProperty
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

object KafkaProperties {
  /**
    * Kafka生产者相关参数
    */
  val kafkaProducerProperties = Map[String, Object](
    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> ServiceProperty.properties.get("kafka.bootstrap.servers").get,

    ProducerConfig.CLIENT_ID_CONFIG -> ServiceProperty.properties.get("kafka.producer.client.id").getOrElse("data-service-default-client"),

    ProducerConfig.ACKS_CONFIG -> "1",
    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer]
  )

  /**
    * Kafka消费者相关参数
    */
  val kafkaConsumerProperties = Map[String, Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> ServiceProperty.properties.get("kafka.bootstrap.servers").get,

    ConsumerConfig.GROUP_ID_CONFIG -> ServiceProperty.properties.get("kafka.consumer.group.id").getOrElse("data-service-default-group"),
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> ServiceProperty.properties.get("kafka.consumer.offset.reset").getOrElse("smallest"),

    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringSerializer],
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
  )
}
