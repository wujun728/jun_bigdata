package com.service.data.spark.streaming.offset
import com.service.data.commons.property.ServiceProperty
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

/**
  * @author 伍鲜
  *
  *         Zookeeper实现Kafka偏移量管理
  */
class KafkaOffsetHandlerZookeeper extends KafkaOffsetHandler {

  /**
    * 读取偏移量
    *
    * @param group
    * @param topic
    * @return
    */
  override def readOffset(group: String, topic: String): Map[TopicPartition, Long] = {
    // 创建一个 ZKGroupTopicDirs 对象
    val topicDirs = new ZKGroupTopicDirs(group = group, topic = topic)

    // 获取 Zookeeper 中的路径，这里会变成 /consumers/${group}/offsets/${topic}
    val topicPath = s"${topicDirs.consumerOffsetDir}"

    // Zookeeper 的 host 和 ip，创建一个 client
    val client = new ZkClient(ServiceProperty.properties.get("kafka.offset.handler.zookeeper.url").get)

    //查询该路径下是否字节点（默认有字节点为我们自己保存不同 partition 时生成的）
    val children = client.countChildren(s"${topicPath}")

    //如果 zookeeper 中有保存 offset，我们会利用这个 offset 作为 kafkaStream 的起始位置
    var fromOffsets: Map[TopicPartition, Long] = Map()

    for (i <- 0 until children) {
      val partitionOffset = client.readData[String](s"${topicDirs.consumerOffsetDir}/${i}")
      //将不同 partition 对应的 offset 增加到 fromOffsets 中
      fromOffsets += (new TopicPartition(topic, i) -> partitionOffset.toLong)
    }

    // 最终得到的偏移量信息
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
    // 创建一个 ZKGroupTopicDirs 对象
    val topicDirs = new ZKGroupTopicDirs(group = group, topic = topic)

    // 获取 Zookeeper 中的路径，这里会变成 /consumers/${group}/offsets/${topic}
    val topicPath = s"${topicDirs.consumerOffsetDir}"

    // Zookeeper 的 host 和 ip，创建一个 client
    val client = new ZkClient(ServiceProperty.properties.get("kafka.offset.handler.zookeeper.url").get)

    for (o <- offsetRanges) {
      val zkPath = s"${topicPath}/${o.partition}"
      //将该 partition 的 offset 保存到 zookeeper
      ZkUtils.updatePersistentPath(client, zkPath, o.untilOffset.toString)
    }
  }
}