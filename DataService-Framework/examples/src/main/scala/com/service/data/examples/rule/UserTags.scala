package com.service.data.examples.rule

import com.service.data.commons.logs.Logging
import com.service.data.examples.rule.entity.User
import com.service.data.examples.rule.rules.TagRules
import hammurabi.{RuleEngine, WorkingMemory}

/**
  * 客户标签
  *
  * 根据客户的某些属性，给客户打标签
  */
object UserTags extends Logging {
  def main(args: Array[String]): Unit = {
    val ruleEngine = RuleEngine(new TagRules().rules)

    val tom = new User("user_tom", Array("研究员", "宝宝"))
    val joe = new User("user_joe", Array("大学", "书包"))
    val fred = new User("user_fred", Array("育儿", "汽车"))
    val bob = new User("user_bob", Array("老大", "四级"))

    val workingMemory = WorkingMemory(Set(tom, joe, fred, bob))

    debug("使用规则前的数据")
    Set(tom, joe, fred, bob).foreach(println)

    ruleEngine execOn workingMemory

    debug("使用规则后的数据")
    Set(tom, joe, fred, bob).foreach(println)
  }
}
