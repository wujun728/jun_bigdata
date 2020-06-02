package com.service.data

import com.service.data.commons.utils.EncryptUtil

object TestEncrypt extends App {
  val username = "username"
  println("username is : ${3DES}%s" format EncryptUtil.encrypt3DES(username))
  val password = "password"
  println("password is : ${3DES}%s" format EncryptUtil.encrypt3DES(password))
}
