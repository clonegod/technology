package sample.anonymous_function

object Fn extends App {
  
  // shorthand define functions: Int => Int
  val fn1 = (x: Int) => x + 1
  println(fn1(3))
  
  // define functions with multiple parameters: (Int, Int) => String
  val fn2 = (x: Int, y: Int) => "(" + x + ", " + y + ")"
  println(fn2(1,2))
  
  // with no parameter: () => String
  val fn3 = () => { System.getProperty("user.dir") }
  println(fn3())
  
  
}