package com.service.data.commons.utils

import java.util.Random

/**
  * @author 伍鲜
  *
  *         字符串工具类
  */
class StringUtil {

}

/**
  * @author 伍鲜
  *
  *         字符串工具类
  */
object StringUtil {
  /**
    * 判断字符串是否包含在给定的字符串中
    *
    * @param str
    * @param args
    * @return
    */
  def inStringIgnoreCase(str: String, args: String*): Boolean = !args.filter(_.equalsIgnoreCase(str)).isEmpty

  def getRandomString(length: Int): String = {
    val base = "aA0bBcC1dDeE2fFgG3hHiI4jJkK5lLmM6nNoO7pPqQ8rRsS9tTuUvVwWxXyYzZ"
    val random = new Random

    (0 until length).map(x => {
      base.charAt(random.nextInt(base.length))
    }).mkString("")
  }
}