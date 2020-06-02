package com.service.data.spark.streaming.process

import scala.util.parsing.json.JSON

/**
  * @author 伍鲜
  *
  *         默认的JSON数据处理实现类，处理JSON格式的Topic数据，不支持多层嵌套。
  */
class DefaultJsonProcess extends TopicValueProcess {
  /**
    * 将Topic的值转换成所需要的字段值数组
    *
    * @param value   Topic 的值
    * @param split   字段分隔符
    * @param columns 字段列表
    * @return 根据Topic的值转换得到的字段值数组
    */
  override def convertToColumns(value: String, split: String, columns: Array[(String, String, Int, String)]): Array[String] = {
    val json = JSON.parseFull(value).getOrElse(Map.empty[String, String]).asInstanceOf[Map[String, String]]
    columns.map(column => json.get(column._1).getOrElse(""))
  }
}
