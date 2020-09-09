package com.service.data.commons.property

import java.util.ServiceLoader

/**
  * @author 伍鲜
  *
  *         应用程序参数配置接口
  */
trait ServicePropertyHandler {
  /**
    * 获取参数配置
    *
    * @return
    */
  def getProperties: Map[String, String]
}

/**
  * @author 伍鲜
  *
  *         应用程序参数配置工厂类
  */
object SparkPropertyHandlerFactory {

  /**
    * 获取应用程序参数配置具体实现
    *
    * @return
    */
  def getSparkPropertyHandler(className: String = classOf[ServicePropertyHandlerProperties].getName): ServicePropertyHandler = {
    val sl = ServiceLoader.load(classOf[ServicePropertyHandler])
    val iterator = sl.iterator()
    var instance: ServicePropertyHandler = null
    while (iterator.hasNext) {
      val obj = iterator.next()
      if (obj.getClass.getName == className) {
        instance = obj
      }
    }
    if (instance == null) {
      throw new NoClassDefFoundError(s"Not found class: ${className}")
    } else {
      instance
    }
  }
}