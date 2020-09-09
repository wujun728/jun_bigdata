package com.service.data.spark.streaming.process

import java.util
import java.util.ServiceLoader

import com.service.data.commons.PubFunction

/**
  * @author 伍鲜
  *
  *         Kafka Topic 数据处理接口
  */
trait TopicValueProcess extends PubFunction {
  /**
    * 将Topic的值转换成所需要的字段值数组
    *
    * @param value   Topic 的值
    * @param split   字段分隔符
    * @param columns 字段列表
    * @return 根据Topic的值转换得到的字段值数组
    */
  def convertToColumns(value: String, split: String, columns: Array[(String, String, Int, String)]): Array[String]
}

/**
  * @author 伍鲜
  *
  *         Kafka Topic 数据处理工厂类
  */
object TopicValueProcess {
  private[this] val process: util.Map[String, TopicValueProcess] = new util.HashMap[String, TopicValueProcess]()

  def getProcess(className: String): TopicValueProcess = {
    TopicValueProcess.synchronized({
      if (!process.containsKey(className)) {
        val sl = ServiceLoader.load(classOf[TopicValueProcess])
        val iterator = sl.iterator()

        var instance: TopicValueProcess = null

        while (iterator.hasNext) {
          val obj = iterator.next()
          // 初始化所有的实现类
          if (!process.containsKey(obj.getClass.getName)) {
            process.put(obj.getClass.getName, obj)
          }
          // 得到具体的实现类
          if (obj.getClass.getName == className) {
            instance = obj
          }
        }
        if (instance == null) {
          throw new Exception("没有找到实现类")
        }
      }
    })
    // 根据className得到实例
    process.get(className)
  }
}
