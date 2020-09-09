package com.service.data.examples.rule.entity

import scala.collection.mutable

case class User(uid: String, apps: Array[String]) {
  val tags = mutable.Set.empty[String]

  override def toString: String = "uid: %s, apps: %s, tags: %s".format(uid, apps.mkString(","), tags)
}
