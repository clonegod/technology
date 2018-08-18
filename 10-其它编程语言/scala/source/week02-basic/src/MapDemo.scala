

object MapDemo extends App {
  
  val a = Map(1 -> "abc", 2 -> "bcd")
  
  // 直接将元组中的变量声明处理，然后通过元组中的变量来取值
  for ((x, y) <- a) {
    print("key is "); println(x)
    print("value is "); println(y)
  }
  
  // 直接将map中的entry赋值给一个元组，再从元组中取各个元素
  for (tuple <- a) {
    print("key is: "); println(tuple._1)
    print("value is: "); println(tuple._2)
  }
  
  println(a(1))
  
  println(a get 1) // 简写方式：当被调用的方法只有1个入参时，可以省略.和()
  println(a.get(1))
  
  println(a.getOrElse(1, "a"))
  
}
