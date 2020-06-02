package hammurabi

/**
  * @author Mario Fusco
  */
object Func {

  /**
    * 集合操作
    *
    * @param l 需要操作的集合
    * @tparam A 集合元素类型
    * @return
    */
  implicit def toListHelper[A](l: List[A]) = new {

    /**
      * 集合中添加元素
      *
      * @param item 需要添加的元素
      * @tparam B 元素类型
      * @return
      */
    def +?[B <: A](item: Option[B]) = item match {
      case Some(b) => b :: l
      case None => l
    }
  }
}
