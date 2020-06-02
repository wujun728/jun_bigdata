package com.service.data.spark.sql.property

import java.io.{File, FileInputStream}
import java.util.Properties

import com.service.data.commons.property.ServicePropertyHandler
import org.apache.spark.SparkEnv

import scala.collection.JavaConversions._
import scala.util.Try

/**
  * @author 伍鲜
  *
  *         通过 spark-submit --files 获取参数配置
  */
class ServicePropertyHandlerSubmit extends ServicePropertyHandler {

  /**
    * 获取参数配置
    *
    * @return
    */
  override def getProperties: Map[String, String] = {
    if (Try(Option(SparkEnv.get.conf.get("spark.yarn.dist.files"))).isSuccess) {
      Option(SparkEnv.get.conf.get("spark.yarn.dist.files")).getOrElse("").split(",", -1)
        .map(file => {
          val properties = new Properties()
          properties.load(new FileInputStream(new File(file).getName))
          properties.toMap
        }).fold(Map[String, String]())(_ ++ _)
    } else {
      Map[String, String]()
    }
  }
}
