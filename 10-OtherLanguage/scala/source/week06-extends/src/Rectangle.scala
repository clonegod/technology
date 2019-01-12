
/**
 * 长方形类定义
 * 	继承Shape类，重写Shape类中定义的抽象方法
 */
case class Rectangle(val height:Double, val width:Double, override val name:String) 
  extends Shape(name) {
  
  override def shapeType = "rectangle"
  
  override def area = height * width
  
}