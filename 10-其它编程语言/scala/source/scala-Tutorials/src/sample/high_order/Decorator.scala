package sample.high_order

class Decorator(left: String, right: String) {
  
  def layout[A](x: A) = left + x.toString() + right
  
}

object FunTest extends App {
  def apply(fn: Int => String, v: Int) = fn(v)
  
  val decorator = new Decorator("[", "]")
  
  println(apply(decorator.layout, 7))

}