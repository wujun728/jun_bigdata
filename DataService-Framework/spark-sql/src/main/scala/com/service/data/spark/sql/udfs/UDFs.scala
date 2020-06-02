package com.service.data.spark.sql.udfs

import com.service.data.commons.tools.IdWorker
import com.service.data.commons.utils.CommUtil
import org.apache.spark.SparkEnv

import scala.util.Random

/**
  * 自定义函数
  */
object UDFs {

  /**
    * 分布式唯一ID生成器
    */
  lazy val idWorker: IdWorker = {
    var id = SparkEnv.get.executorId
    id = id match {
      case "driver" => "31"
      case _ => id
    }
    new IdWorker(id.toLong % 32, Random.nextInt(32))
  }

  /**
    * 分布式唯一ID生成
    *
    * @return
    */
  def GenerateID: String = {
    s"${idWorker.nextId()}"
  }

  /**
    * Decimal类型的数据保留指定精度。Spark SQL中的round函数，只支持所有行采用统一精度，不支持不同行采用不同精度。
    *
    * @param value
    * @param scale
    * @return
    */
  def roundDecimal(value: java.math.BigDecimal, scale: Int): java.math.BigDecimal = {
    if (CommUtil.isEmpty(value)) {
      new java.math.BigDecimal(0)
    } else {
      value.setScale(scale, java.math.BigDecimal.ROUND_HALF_UP)
    }
  }

  /**
    * Decimal类型的数据保留指定精度。Spark SQL中的bround函数，只支持所有行采用统一精度，不支持不同行采用不同精度。
    *
    * @param value
    * @param scale
    * @return
    */
  def broundDecimal(value: java.math.BigDecimal, scale: Int): java.math.BigDecimal = {
    if (CommUtil.isEmpty(value)) {
      new java.math.BigDecimal(0)
    } else {
      value.setScale(scale, java.math.BigDecimal.ROUND_HALF_EVEN)
    }
  }
}
