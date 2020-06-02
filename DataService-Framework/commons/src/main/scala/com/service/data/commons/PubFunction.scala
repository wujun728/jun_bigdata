package com.service.data.commons

import java.io.InputStream
import java.util.Properties

/**
  * @author 伍鲜
  *
  *         公共方法
  */
trait PubFunction {
  private[this] val regReplace = Map("|" -> "\\|", "^" -> "\\^", "*" -> "\\*", "+" -> "\\+", "$" -> "\\$", "?" -> "\\?", "." -> "\\.")

  /**
    * 获取配置文件信息
    *
    * @param file 配置文件
    * @return
    */
  def PropertiesFromFile(file: String): Properties = {
    val props: Properties = new Properties()
    val files = Thread.currentThread().getContextClassLoader().getResources(file)
    var in: InputStream = null
    while (files.hasMoreElements()) {
      try {
        in = files.nextElement().openStream()
        props.load(in)
      } finally {
        try {
          in.close()
        } catch {
          case ex: Exception => /* ignore */ ex.toString()
        }
      }
    }
    props
  }

  /**
    * 十六进制转换成字符串
    *
    * @param hex 十六进制编码
    * @return
    */
  def Hex2String(hex: String): String = {
    val builder: StringBuilder = new StringBuilder()

    val pattern = "[^0-9a-fA-F]".r

    if (hex.length % 2 == 0 && "".equals(pattern findFirstIn hex getOrElse (""))) {
      for (i <- 0 to hex.length / 2 - 1) {
        builder.append(Integer.parseInt(hex.substring(i * 2, (i + 1) * 2), 16).toChar)
      }
      builder.toString()
    } else {
      throw new Exception("非法的16进制数据")
    }
  }

  /**
    * 将正则表达式中的特殊字符转义
    *
    * @param regexp 待转义的正则表达式
    * @return 将特殊字符转义后的字符串
    */
  def RegexpEscape(regexp: String): String = {
    var result = regexp
    regReplace.keySet.foreach(key => result = result.replace(key, regReplace(key)))
    result
  }

}
