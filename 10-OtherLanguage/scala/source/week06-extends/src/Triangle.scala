
/**
 * 三角形定义
 * 	继承Shape类，重写Shape类中定义的抽象方法
 *  
 */
case class Triangle(val side1:Double, val side2:Double, val side3:Double, override val name:String) 
  extends Shape(name){
  
  override def shapeType = "triangle"
  
  // 如果函数没有任何参数，则省略方法的括号
  override def area = {
    val s = (side1 + side2 + side3) / 2
    math.sqrt(s * (s - side1) * (s - side2) * (s - side3))
  }
  
}