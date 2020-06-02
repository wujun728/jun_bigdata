package com.service.data.commons.property

import java.util.Properties
import scala.collection.JavaConversions._

/**
  * @author 伍鲜
  *
  *         通过属性文件获取参数配置
  */
class ServicePropertyHandlerProperties extends ServicePropertyHandler {
  /**
    * 获取参数配置
    *
    * @return
    */
  override def getProperties: Map[String, String] = {
    val props: Properties = new Properties()
    props.load(getClass.getClassLoader.getResource("application.properties").openStream())
    props.toMap
  }
}
