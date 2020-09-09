package com.service.data.kafka.streams

import java.time.Duration
import java.util.Properties

import com.service.data.commons.PubFunction
import com.service.data.commons.property.ServiceProperty
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.Predicate
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

/**
  * @author 伍鲜
  *
  *         Kafka Topic 的数据过滤
  */
class KafkaStreamsTopicFilter {

}

/**
  * @author 伍鲜
  *
  *         Kafka Topic 的数据过滤
  */
object KafkaStreamsTopicFilter extends App with PubFunction {

  val props = new Properties()

  // 相关参数配置
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, ServiceProperty.properties.get("kafka.streams.application.id").getOrElse("KafkaStreamsApplicationID"))
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ServiceProperty.properties.get("kafka.bootstrap.servers").get)
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)

  val builder: StreamsBuilder = new StreamsBuilder

  ServiceProperty.properties.keySet.toArray
    // 筛选满足指定格式的Key
    .filter(k => !"kafka.streams.(.*).source.topic".r.findAllIn(k.toString).isEmpty)
    // 截取指定位置的字符串，去重后得到相关分组
    .map(k => k.toString.split(RegexpEscape("."), -1)(2)).distinct
    // 对每一个组进行处理
    .foreach(group => {
    builder
      // 找到该组中的数据源Topic
      .stream(ServiceProperty.properties.get(s"kafka.streams.${group}.source.topic").get)
      .filter(new Predicate[String, String] {
        override def test(k: String, v: String): Boolean = {
          // 筛选出该Topic中满足指定正则表达式的数据
          !ServiceProperty.properties.get(s"kafka.streams.${group}.regex.string").get.r.findAllIn(v).isEmpty
        }
      })
      // 将满足条件的数据写入到目标Topic
      .to(ServiceProperty.properties.get(s"kafka.streams.${group}.target.topic").get)
  })

  val streams = new KafkaStreams(builder.build(), props)
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }
}
