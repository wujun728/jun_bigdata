package com.service.data.commons.utils

import java.security.{InvalidKeyException, NoSuchAlgorithmException}
import java.util.Random
import javax.crypto._

import com.service.data.commons.encrypt.DES
import sun.misc.{BASE64Decoder, BASE64Encoder}

/**
  * @author 伍鲜
  *
  *         加密工具类
  */
class EncryptUtil {

}

/**
  * @author 伍鲜
  *
  *         加密工具类
  */
object EncryptUtil {
  def get3DesKey: Array[Byte] = {
    val retKey = new Array[Byte](24)
    val rd = new Random
    val rl = rd.nextLong * System.currentTimeMillis + rd.nextLong
    val base = rl.toString.getBytes
    val blen = base.length
    val rd1 = new Random(rl)
    val key = new Array[Byte](blen * 4)
    rd.nextBytes(key)
    for (i <- 0 until blen) {
      val idx = 3 * i
      key(idx) = (base(i) + key(idx) & 0xFF).toByte
      key(idx + 1) = (key((i + 1)) & key(idx) & 0xFF).toByte
      key(idx + 2) = ((key((i + 2)) ^ key((idx + 1))) & 0xFF).toByte
      key(idx + 3) = ((key((i + 3)) | key((idx + 2))) & 0xFF).toByte
    }
    val start = rd1.nextInt % key.length + key.length
    for (i <- 0 to 23) {
      retKey(i) = key((start + i) % key.length)
    }
    retKey
  }

  def getDesKey: Array[Byte] = {
    val retKey = new Array[Byte](8)
    val key = get3DesKey
    System.arraycopy(key, 8, retKey, 0, 8)
    retKey
  }

  def encodeBase64(byteArray: Array[Byte]): String = {
    val base64String = new BASE64Encoder().encode(byteArray)
    base64String
  }

  def decodeBase64(base64String: String): Array[Byte] = {
    try {
      new BASE64Decoder().decodeBuffer(base64String)
    } catch {
      case ex: Exception =>
        ex.toString
        "".getBytes
    }
  }

  @throws[IllegalBlockSizeException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[NoSuchPaddingException]
  def encrypt3DES(str: String): String = {
    if (str == null) return str
    val tmp = str.getBytes
    val len = tmp.length * 2
    val rd = new Random
    val tmp1 = new Array[Byte](len)
    rd.nextBytes(tmp1)
    for (i <- 0 until tmp.length) {
      tmp1(2 * i) = tmp(i)
    }
    encodeBase64(DES.encrypt3DES(tmp1, DES.getDesKey))
  }

  @throws[IllegalBlockSizeException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[NoSuchPaddingException]
  def decrypt3DES(str: String): String = {
    if (str == null) return str
    val tmp1 = DES.decrypt3DES(decodeBase64(str), DES.getDesKey)
    val tmp = new Array[Byte](tmp1.length / 2)
    for (i <- 0 until tmp.length) {
      tmp(i) = tmp1(2 * i)
    }
    new String(tmp)
  }
}
