
/**
 * 抽象类-使用abstract进行标识，其中的抽象方法不需要使用abstract标识
 * 
 */
abstract class Shape(val name: String) {
  def shapeType: String
  
  def area: Double
}