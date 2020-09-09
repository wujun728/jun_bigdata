package com.service.data.spark.sql.implicits

import com.service.data.commons.logs.Logging
import org.apache.spark.sql.{Dataset, SparkDataset}

import scala.util.Try

object SparkSqlImplicit {

  /**
    * 扩展Dataset的方法
    *
    * @param ds
    */
  implicit class DatasetUtil(val ds: Dataset[_]) extends Logging {

    /**
      * 判断是否包含指定的列
      *
      * @param columnName 列名称
      * @return
      */
    def hasColumn(columnName: String): Boolean = {
      Try(ds(columnName)).isSuccess
    }

    /**
      * 判断是否包含指定的全部列
      *
      * @param columnNames 列名称
      * @return
      */
    def hasAllColumns(columnNames: Seq[String]): Boolean = {
      columnNames.map(columnName => Try(ds(columnName)).isSuccess).reduce(_ && _)
    }

    /**
      * 通过debug打印数据集内容
      */
    def debugShow(): Unit = debugShow(20)

    /**
      * 通过debug打印数据集内容
      *
      * @param numRows 打印多少行
      */
    def debugShow(numRows: Int): Unit = debugShow(numRows, false)

    /**
      * 通过debug打印数据集内容
      *
      * @param numRows  打印多少行
      * @param truncate 是否截取超长内容
      */
    def debugShow(numRows: Int, truncate: Boolean): Unit = {
      debug("数据集的内容为：\n" + SparkDataset.showString(ds, numRows, truncate))
    }
  }

}
