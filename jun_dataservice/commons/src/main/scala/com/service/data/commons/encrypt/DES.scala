package com.service.data.commons.encrypt

import java.security.{InvalidKeyException, NoSuchAlgorithmException}
import javax.crypto._
import javax.crypto.spec.SecretKeySpec

/**
  * @author 伍鲜
  *
  *         DES加密算法
  */
class DES {

}

/**
  * @author 伍鲜
  *
  *         DES加密算法
  */
object DES {
  private val des3Key = "1234567890qwertyuiopzdlw".getBytes

  private val DES3 = "DESede"

  private val DES = "DES"

  def getDesKey: Array[Byte] = des3Key

  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  def encrypt3DES(value: Array[Byte], key: Array[Byte]): Array[Byte] = {
    val c1 = Cipher.getInstance(DES3)
    c1.init(1, new SecretKeySpec(key, DES3))
    c1.doFinal(value)
  }

  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  def decrypt3DES(value: Array[Byte], key: Array[Byte]): Array[Byte] = {
    val c1 = Cipher.getInstance(DES3)
    c1.init(2, new SecretKeySpec(key, DES3))
    c1.doFinal(value)
  }

  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  def encryptDES(value: Array[Byte], key: Array[Byte]): Array[Byte] = {
    val c1 = Cipher.getInstance(DES)
    c1.init(1, new SecretKeySpec(key, DES))
    c1.doFinal(value)
  }

  @throws[NoSuchPaddingException]
  @throws[NoSuchAlgorithmException]
  @throws[InvalidKeyException]
  @throws[BadPaddingException]
  @throws[IllegalBlockSizeException]
  def decryptDES(value: Array[Byte], key: Array[Byte]): Array[Byte] = {
    val c1 = Cipher.getInstance(DES)
    c1.init(2, new SecretKeySpec(key, DES))
    c1.doFinal(value)
  }
}