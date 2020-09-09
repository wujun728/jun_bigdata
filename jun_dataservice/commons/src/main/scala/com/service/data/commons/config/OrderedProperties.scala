package com.service.data.commons.config

import java.io.{FileOutputStream, IOException}
import java.util
import java.util._

import scala.collection.JavaConversions._

/**
  * @author 伍鲜
  *
  *         排序的Properties
  */
class OrderedProperties extends Properties {

  private val allKeys = new util.LinkedHashSet[AnyRef]

  override def keys: util.Enumeration[AnyRef] = Collections.enumeration[AnyRef](allKeys)

  override def put(key: AnyRef, value: AnyRef): AnyRef = {
    allKeys.add(key)
    super.put(key, value)
  }

  override def keySet: util.Set[AnyRef] = allKeys

  override def stringPropertyNames: util.Set[String] = {
    val set = new util.LinkedHashSet[String]
    for (key <- this.allKeys) {
      set.add(key.asInstanceOf[String])
    }
    set
  }

  @throws[IOException]
  def saveProperties(file: String): Unit = {
    val fos: FileOutputStream = new FileOutputStream(file)
    store(fos, "Properties")
    if (fos != null) {
      try {
        fos.close()
      } catch {
        case ex: IOException => ex.toString
      }
    }
  }
}
