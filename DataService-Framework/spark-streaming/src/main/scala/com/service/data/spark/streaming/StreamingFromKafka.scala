package com.service.data.spark.streaming

import java.text.SimpleDateFormat

import com.service.data.commons.PubFunction
import com.service.data.commons.dbs.DBConnection
import com.service.data.commons.property.ServiceProperty
import com.service.data.commons.utils.CommUtil
import com.service.data.spark.streaming.offset.KafkaOffsetHandlerFactory
import com.service.data.spark.streaming.process.TopicValueProcess
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkEnv}
import scalikejdbc._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author 伍鲜
  *
  *         Spark Streaming 消费 Kafka 的数据
  */
class StreamingFromKafka {

}

/**
  * @author 伍鲜
  *
  *         Spark Streaming 消费 Kafka 的数据
  */
object StreamingFromKafka extends PubFunction {
  def main(args: Array[String]): Unit = {
    /**
      * 主题对应的表。(主题， 表名， 字段分隔符)
      */
    var topicTables: Array[(String, String, String)] = Array.empty[(String, String, String)]

    /**
      * 表包含的字段。（表名， 字段名称， 字段类型， 字段顺序， 字段值）
      */
    var tableColumns: Array[(String, String, String, Int, String)] = Array.empty[(String, String, String, Int, String)]

    /**
      * 主题的码值转换。（主题， 字段名称， 源代码值， 目标代码值）
      */
    var topicCodeMapping: Array[(String, String, String, String)] = Array.empty[(String, String, String, String)]

    DBConnection.setupAll()
    NamedDB('config) readOnly ({ implicit session =>
      topicTables = sql"select topic_name,table_name,field_split from app_t_topic_table_list".map(rs => (rs.string(1), rs.string(2), rs.string(3))).list().apply().toArray
      tableColumns = sql"select table_name,column_name,column_type,column_index,column_data from app_t_table_column_list".map(rs => (rs.string(1), rs.string(2), rs.string(3), rs.int(4), rs.string(5))).list().apply().toArray
      topicCodeMapping = sql"select topic_name,column_name,source_code,target_code from app_t_topic_code_mapping".map(rs => (rs.string(1), rs.string(2), rs.string(3), rs.string(4))).list().apply().toArray
    })

    val tableMap = topicTables.map(table => Map(table._1 -> table)).reduce(_ ++ _)
    val columnMap = tableColumns.groupBy(_._1).map(column => (column._1 -> column._2.sortWith(_._4 < _._4).map(x => (x._2, x._3, x._4, x._5))))
    val mappingMap = topicCodeMapping.groupBy(_._1).map(mapping => (mapping._1, mapping._2.map(x => (x._2, x._3, x._4))))

    // 设置Hadoop用户名称
    System.setProperty("HADOOP_USER_NAME", "hadoop")
    val sparkConf = new SparkConf()
      .setAppName(ServiceProperty.properties.get("spark.streaming.application.name").getOrElse("SparkStreamingKafkaToDatabase"))
      // IDEA本地直接提交到Yarn执行，需要在resources中添加Hadoop、Spark等的conf文件
      .setMaster("yarn-client").set("yarn.resourcemanager.hostname", "").set("spark.executor.instances", "1").setJars(Seq())
    // 部署打包的时候需要去掉下面这行
    // .setMaster("local[3]")

    val ssc = new StreamingContext(sparkConf, Seconds(ServiceProperty.properties.get("spark.streaming.batch.duration").getOrElse("5").toLong))

    if (!"nocp".equalsIgnoreCase(ServiceProperty.properties.get("spark.streaming.checkpoint.dir").getOrElse("nocp"))) {
      ssc.checkpoint(ServiceProperty.properties.get("spark.streaming.checkpoint.dir").get)
    }

    // Kafka配置参数
    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> ServiceProperty.properties.get("kafka.bootstrap.servers").getOrElse(""),
      ConsumerConfig.GROUP_ID_CONFIG -> ServiceProperty.properties.get("kafka.consumer.group.id").getOrElse("data-service-default-group"),
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> ServiceProperty.properties.get("kafka.consumer.offset.reset").getOrElse("smallest"),
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
    )

    // 消费的主题
    val topics = ServiceProperty.properties("spark.streaming.consumer.topics").split(",", -1)

    // 获取Kafka偏移量
    val fromOffsets: Map[TopicPartition, Long] = KafkaOffsetHandlerFactory.getKafkaOffsetHandler().readOffset(kafkaParams(ConsumerConfig.GROUP_ID_CONFIG).toString, topics)

    topics.foreach(topic => {

      // 获取Kafka偏移量
      val fromOffsets: Map[TopicPartition, Long] = KafkaOffsetHandlerFactory.getKafkaOffsetHandler().readOffset(kafkaParams(ConsumerConfig.GROUP_ID_CONFIG).toString, topics)

      // 根据偏移量获取Kafka的数据
      implicit val stream = if (fromOffsets.size > 0) {
        KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams, fromOffsets))
      } else {
        KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))
      }

      stream.foreachRDD(rdd => {
        // 无数据，不执行
        if (!rdd.isEmpty()) {
          // 记录偏移量
          val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
          offsetRanges.foreach(println)

          // Partition的数据在Executor上运行
          rdd.foreachPartition(data => {
            // 根据主题取表
            val table = tableMap(topic)
            // 根据表名取字段
            val columns = columnMap(table._2)
            Future {
              process(StreamingMessage(table, columns, data.map(_.value()), mappingMap.get(topic).getOrElse(Array()), ServiceProperty.properties.get(s"spark.${topic}.process.class").getOrElse("com.service.data.spark.streaming.process.DefaultTextProcess")))
            }
          })
          // 提交偏移量
          KafkaOffsetHandlerFactory.getKafkaOffsetHandler().writeOffset(kafkaParams(ConsumerConfig.GROUP_ID_CONFIG).toString, topics, offsetRanges)
        }
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * 数据类型转换
    *
    * @param column 字段配置
    * @return
    */
  private def convert(column: ((String, String, Int, String), String)): Any = {
    if (CommUtil.isEmpty(column._2)) {
      ""
    } else {
      try {
        column._1._2.toLowerCase.trim match {
          case "int" => column._2.toInt
          case "long" => column._2.toLong
          case "double" => column._2.toDouble

          case "date" => new SimpleDateFormat(column._1._4).parse(column._2)
          case "datetime" => new SimpleDateFormat(column._1._4).parse(column._2)
          case "timestamp" => new SimpleDateFormat(column._1._4).parse(column._2)

          case _ => column._2
        }
      } catch {
        case ex: Exception =>
          println(s"${SparkEnv.get.executorId}:${Thread.currentThread.getId}-${Thread.currentThread.getName}:${ex.getMessage}")
          null
      }
    }
  }

  /**
    * 处理消费到的数据
    *
    * @param message 消费消息
    */
  private def process(message: StreamingMessage): Unit = {
    try {
      val dataList = message.rdd
        // 得到数据解析后的字段值
        .map(x => TopicValueProcess.getProcess(message.className).convertToColumns(x, message.table._3, message.columns))
        // 过滤掉字段个数不满足要求的数据
        .filter(_.length >= message.columns.length)
        // 将字段与字段值整合
        .map(x => message.columns.zip(x))
        // 对字段值进行码值转换
        .map(x => x.map(column => (column._1, message.mapping.filter(_._1 == column._1._1).filter(_._2 == column._2).map(_._3).headOption.getOrElse(column._2))))
        // 数据类型转换
        .map(x => x.map(convert(_)))
        // 转换成序列
        .map(_.toSeq)
        // 序列
        .toSeq

      val insert = s"insert into ${message.table._2} (${message.columns.map(_._1).mkString(",")})"
      val values = s"values (${message.columns.map(x => "?").mkString(",")})"

      try {
        // 验证数据源是否初始化
        NamedDB('config) localTx { implicit session =>
          sql"select count(*) from app_t_param_config_list".map(rs => rs.int(1)).single().apply()
        }
      } catch {
        case ex: Exception =>
          println(s"${SparkEnv.get.executorId}:${Thread.currentThread.getId}-${Thread.currentThread.getName}:${ex.getMessage}")
          DBConnection.setupAll()
      }
      try {
        // 将数据保存到云环境
        NamedDB('mysql) localTx { implicit session =>
          SQL(s"${insert}${values}").batch(dataList: _*).apply()
        }
      } catch {
        case ex: Exception =>
          println(s"${SparkEnv.get.executorId}:${Thread.currentThread.getId}-${Thread.currentThread.getName}:${ex.getMessage}")
      }
      try {
        // 将数据保存到集市
        NamedDB('oracle) localTx { implicit session =>
          SQL(s"${insert}${values}").batch(dataList: _*).apply()
        }
      } catch {
        case ex: Exception =>
          println(s"${SparkEnv.get.executorId}:${Thread.currentThread.getId}-${Thread.currentThread.getName}:${ex.getMessage}")
      }
    } catch {
      case ex: Exception =>
        println(s"${SparkEnv.get.executorId}:${Thread.currentThread.getId}-${Thread.currentThread.getName}:${ex.getMessage}")
    }
  }
}
