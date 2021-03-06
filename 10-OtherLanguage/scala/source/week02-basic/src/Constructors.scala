
// class的主构造函数
class Constructors(x: Int, y: Int) {
  
  val x0 = x;
  val y0 = y;
  
  // 辅助构造函数--构造函数的重载
  def this(x: Int) {
    this(x, 1)
  }
  
  // 辅助构造函数--构造函数的重载
  def this() {
    this(1)
  }
}

object ConstructorDemo extends App {
  
  val c1 = new Constructors()
  println(c1.x0)
  println(c1.y0)
  
  val c2 = new Constructors(2)
  println(c2.x0)
  println(c2.y0)
  
  val c3 = new Constructors(3, 3)
  println(c3.x0)
  println(c3.y0)
}