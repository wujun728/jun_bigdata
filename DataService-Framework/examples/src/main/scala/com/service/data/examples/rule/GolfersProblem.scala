package com.service.data.examples.rule

import com.service.data.examples.rule.entity.Person
import hammurabi.Rule._
import hammurabi.{RuleEngine, WorkingMemory}

/**
  * 高尔夫球员位置问题
  *
  * 已知有四个高尔夫球员，他们的名字是Fred、Joe、Bob、Tom
  * 今天他们分别穿着红色，蓝色，橙色，以及格子衣服，并且他们按照从左往右的顺序站成一排
  * 我们将最左边的位置定为1，最右边的位置定为4，中间依次是2、3位置
  *
  * 现在我们了解的情况是：
  * 1、高尔夫球员Fred，目前不知道他的位置和衣服颜色
  * 2、Fred右边紧挨着的球员穿蓝色衣服
  * 3、Joe排在第2个位置
  * 4、Bob穿着格子短裤
  * 5、Tom没有排在第1位或第4位，也没有穿橙色衣服
  *
  * 请问：这四名球员的位置和衣服颜色。
  */
object GolfersProblem {
  def main(args: Array[String]): Unit = {

    // 已知有四个高尔夫球员
    // 他们的名字是Fred、Joe、Bob、Tom
    val fred = new Person("Fred")
    val joe = new Person("Joe")
    val bob = new Person("Bob")
    val tom = new Person("Tom")

    val golfers = Seq(tom, joe, fred, bob)

    // 我们将最左边的位置定为1，最右边的位置定为4，中间依次是2、3位置
    var availablePos = (1 to 4).toSet

    // 今天他们分别穿着红色，蓝色，橙色，以及格子衣服
    var availableColors = Set("red", "blue", "orange", "plaid")

    val ruleSet = Set(
      // 不知道球员的位置
      rule("Unique positions") let {
        val p = any(kindOf[Person])
        when {
          (availablePos.size equals 1) and (p.pos equals 0)
        } then {
          p.pos = availablePos.head
        }
      },

      // 不知道球员穿什么衣服
      rule("Unique colors") let {
        val p = any(kindOf[Person])
        when {
          (availableColors.size equals 1) and (p.color == null)
        } then {
          p.color = availableColors.head
        }
      },

      // 2、Fred右边紧挨着的球员穿蓝色衣服
      rule("Person to Fred's immediate right is wearing blue pants") let {
        val p1 = any(kindOf[Person])
        val p2 = any(kindOf[Person])
        when {
          (p1.name equals "Fred") and (p2.pos equals p1.pos + 1)
        } then {
          p2.color = "blue"
          availableColors = availableColors - p2.color
        }
      },

      // 2、Fred右边紧挨着的球员穿蓝色衣服
      // Fred右边有人，所以Fred不在第4个位置
      rule("Fred isn't in position 4") let {
        val possibleFredPos = availablePos - 4
        val p = any(kindOf[Person])
        when {
          (p.name equals "Fred") and (possibleFredPos.size == 1)
        } then {
          p.pos = possibleFredPos.head
          availablePos = availablePos - p.pos
        }
      },

      // 3、Joe排在第2个位置
      rule("Joe is in position 2") let {
        val p = any(kindOf[Person])
        when {
          p.name equals "Joe"
        } then {
          p.pos = 2
          availablePos = availablePos - p.pos
        }
      },

      // 4、Bob穿着格子短裤
      rule("Bob is wearing plaid pants") let {
        val p = any(kindOf[Person])
        when {
          p.name equals "Bob"
        } then {
          p.color = "plaid"
          availableColors = availableColors - p.color
        }
      },

      // 5、Tom没有排在第1位或第4位，也没有穿橙色衣服
      rule("Tom isn't in position 1 or 4") let {
        val possibleTomPos = availablePos - 1 - 4
        val p = any(kindOf[Person])
        when {
          (p.name equals "Tom") and (possibleTomPos.size equals 1)
        } then {
          p.pos = possibleTomPos.head
          availablePos = availablePos - p.pos
        }
      },

      // 5、Tom没有排在第1位或第4位，也没有穿橙色衣服
      rule("Tom isn't wearing orange pants") let {
        val possibleTomColors = availableColors - "orange"
        val p = any(kindOf[Person])
        when {
          (p.name equals "Tom") and (possibleTomColors.size equals 1)
        } then {
          p.color = possibleTomColors.head
          availableColors = availableColors - p.color
        }
      }
    )

    val workingMemory = WorkingMemory(golfers)

    RuleEngine(ruleSet) execOn workingMemory

    assert(tom.pos == 3)
    assert(tom.color == "red")

    assert(joe.pos == 2)
    assert(joe.color == "blue")

    assert(fred.pos == 1)
    assert(fred.color == "orange")

    assert(bob.pos == 4)
    assert(bob.color == "plaid")

    golfers.sortWith(_.pos < _.pos).foreach(println)
  }
}
