
/**
 * 椭圆类定义
 * 	如果需要使用Pattern Matching， 则将该类定义中声明case标识
 * 	继承Shape类，重写Shape类中定义的抽象方法
 *  使用val声明的参数，将作为成员变量进行使用
 */
case class Elipse(val a:Double, val b:Double, override val name:String) 
  extends Shape(name:String) {
  
  override def shapeType = "elipse"
  
  override def area = a * b * math.Pi
  
}