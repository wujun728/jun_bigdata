package com.service.data.commons.dbs

import com.service.data.commons.logs.Logging
import scalikejdbc.{ConnectionPool, DB, DBConnectionAttributes, NamedDB, SettingsProvider}

/**
  * @author 伍鲜
  *
  *         数据源连接池处理
  */
class DBConnectionPool {

}

/**
  * @author 伍鲜
  *
  *         数据源连接池处理
  */
object DBConnectionPool extends Logging {
  /**
    * 获取默认的数据源连接池
    *
    * @return
    */
  def getDB(): DB = {
    val symbol = 'default
    if (!ConnectionPool.isInitialized(symbol)) {
      debug(s"初始化数据源：${symbol.name}")
      DBConnection.setup(symbol)
    }
    DB(ConnectionPool.borrow(), DBConnectionAttributes(), SettingsProvider.default)
  }

  /**
    * 获取指定名称的数据源连接池
    *
    * @param symbol
    * @return
    */
  def getNamedDB(symbol: Symbol): NamedDB = {
    if (!ConnectionPool.isInitialized(symbol)) {
      debug(s"初始化数据源：${symbol.name}")
      DBConnection.setup(symbol)
    }
    NamedDB(symbol)
  }
}