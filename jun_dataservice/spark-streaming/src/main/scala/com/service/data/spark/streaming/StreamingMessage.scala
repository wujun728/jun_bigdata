package com.service.data.spark.streaming

/**
  * @author 伍鲜
  *
  *         Spark Streaming 处理的数据结构
  * @param table     表信息（主题， 表名，字段分隔符）
  * @param columns   字段信息（字段名称， 字段类型， 字段顺序， 字段值）
  * @param rdd       数据（数据值）
  * @param mapping   码值转换（字段名称， 源代码值， 目标代码值）
  * @param className 处理数据的实现类的名称
  */
case class StreamingMessage(table: (String, String, String), columns: Array[(String, String, Int, String)], rdd: Iterator[String], mapping: Array[(String, String, String)], className: String)
