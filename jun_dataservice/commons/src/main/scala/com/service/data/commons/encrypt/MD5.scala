package com.service.data.commons.encrypt

import java.security.MessageDigest

/**
  * @author 伍鲜
  *
  *         MD5加密算法
  */
class MD5 {

}

/**
  * @author 伍鲜
  *
  *         MD5加密算法
  */
object MD5 {
  def getMD5(str: String): String = {
    try { // 生成一个MD5加密计算摘要
      val md = MessageDigest.getInstance("MD5")
      // 计算md5函数
      md.update(str.getBytes)
      val b = md.digest
      var i = 0
      val buf = new StringBuffer("")
      for (offset <- 0 until b.length) {
        i = b(offset)
        if (i < 0) i += 256
        if (i < 16) buf.append("0")
        buf.append(Integer.toHexString(i))
      }
      // 32位加密
      buf.toString.toUpperCase
      // 16位的加密
      // return buf.toString().substring(8, 24);
    } catch {
      case ex: Exception =>
        ex.toString
        null
    }
  }
}
