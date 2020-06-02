package com.service.data.examples.rule.rules

import com.service.data.examples.rule.entity.User
import com.service.data.rule.RuleTrait
import hammurabi.Rule
import hammurabi.Rule._

class TagRules extends RuleTrait {
  override val rules: Set[Rule] = {
    Set(
      rule("add 母婴 Tag") let {
        val u = any(kindOf[User])
        when {
          u.apps.mkString(",").matches(".*(孕|宝宝|育儿).*")
        } then {
          u.tags += "母婴"
        }
      },
      rule("add 大学生 Tag") let {
        val u = any(kindOf[User])
        when {
          u.apps.mkString(",").matches(".*(四级|六级|大学).*")
        } then {
          u.tags += "大学生"
        }
      }
    )
  }
}
