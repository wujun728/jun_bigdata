package com.service.data.commons.property

import com.service.data.commons.dbs.DBConnectionPool
import scalikejdbc._

/**
  * @author 伍鲜
  *
  *         通过数据库获取参数配置
  */
class ServicePropertyHandlerDatabase extends ServicePropertyHandler {
  /**
    * 获取参数配置
    *
    * @return
    */
  override def getProperties: Map[String, String] = {
    DBConnectionPool.getNamedDB('config) readOnly ({ implicit session =>
      sql"select param_name,param_value from app_t_param_config_list".map(rs => Map(rs.string(1) -> rs.string(2))).list().apply().reduce(_ ++ _)
    })
  }
}
