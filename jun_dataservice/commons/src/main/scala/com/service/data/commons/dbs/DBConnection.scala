package com.service.data.commons.dbs

import com.service.data.commons.config.OrderedProperties
import com.service.data.commons.utils.EncryptUtil
import scalikejdbc.{ConnectionPool, ConnectionPoolSettings}

/**
  * @author 伍鲜
  *
  *         数据源处理
  */
class DBConnection {

}

/**
  * @author 伍鲜
  *
  *         数据源处理
  */
object DBConnection {

  private[this] val props: OrderedProperties = new OrderedProperties()
  private[this] val resource = getClass.getClassLoader.getResource("db.properties")

  // 加载配置文件
  props.load(resource.openStream())

  /**
    * 对指定数据源进行初始化
    *
    * @param symbol 指定数据源
    */
  def setup(symbol: Symbol): Unit = {
    val driver = props.getProperty(s"db.${symbol.name}.driver")
    val url = props.getProperty(s"db.${symbol.name}.url")
    var user = props.getProperty(s"db.${symbol.name}.user")
    var password = props.getProperty(s"db.${symbol.name}.password")

    if (user.startsWith("${3DES}")) {
      user = EncryptUtil.decrypt3DES(user.substring(7))
    }
    if (password.startsWith("${3DES}")) {
      password = EncryptUtil.decrypt3DES(password.substring(7))
    }

    // 加载驱动类
    Class.forName(driver)

    // 设置连接属性
    val settings = ConnectionPoolSettings(
      initialSize = if (props.contains(s"db.${symbol.name}.poolInitialSize")) props.getProperty(s"db.${symbol.name}.poolInitialSize").toInt else 2,
      maxSize = if (props.contains(s"db.${symbol.name}.poolMaxSize")) props.getProperty(s"db.${symbol.name}.poolMaxSize").toInt else 5,
      connectionTimeoutMillis = if (props.contains(s"db.${symbol.name}.connectionTimeoutMillis")) props.getProperty(s"db.${symbol.name}.connectionTimeoutMillis").toInt else 3000L)

    // 初始化连接池
    ConnectionPool.add(symbol, url, user, password, settings)
  }

  /**
    * 对所有数据源进行初始化
    */
  def setupAll(): Unit = {
    props.keySet.toArray.filter(_.toString.startsWith("db.")).map(_.toString.split("\\.", -1)).map(x => x(1)).distinct.foreach(x => setup(Symbol(x)))
  }

  /**
    * 关闭数据源
    *
    * @param symbol
    */
  def close(symbol: Symbol): Unit = {
    ConnectionPool.close(symbol)
  }

  /**
    * 关闭所有数据源
    */
  def closeAll(): Unit = {
    props.keySet.toArray.filter(_.toString.startsWith("db.")).map(_.toString.split("\\.", -1)).map(x => x(1)).distinct.foreach(x => close(Symbol(x)))
  }
}