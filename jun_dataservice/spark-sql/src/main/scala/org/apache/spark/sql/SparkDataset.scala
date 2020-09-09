package org.apache.spark.sql

/**
  * @author 伍鲜
  *
  *         Dataset工具类
  *
  *         单独将该类提取到org.apache.spark.sql包下的原因是：
  *         Spark限制了方法的权限范围：private[sql] def showString(_numRows: Int, truncate: Int = 20): String
  */
object SparkDataset {
  /**
    * 获取Dataset中的前numRows行数据
    *
    * @param ds
    * @param numRows
    * @param truncate
    * @return
    */
  def showString(ds: Dataset[_], numRows: Int, truncate: Boolean): String = {
    if (truncate) {
      // 该方法只能在org.apache.spark.sql包下可访问
      ds.showString(numRows, truncate = 20)
    } else {
      // 该方法只能在org.apache.spark.sql包下可访问
      ds.showString(numRows, truncate = 0)
    }
  }
}
