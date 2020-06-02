package com.service.data.commons.property

import scala.util.Try

/**
  * @author 伍鲜
  *
  *         应用程序参数配置
  */
object ServiceProperty {
  lazy val properties = Try(SparkPropertyHandlerFactory.getSparkPropertyHandler().getProperties).getOrElse(Map[String, String]())
}
