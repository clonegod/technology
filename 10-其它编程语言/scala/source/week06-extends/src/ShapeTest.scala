

object ShapeTest {
  
  def main(args: Array[String]): Unit = {
    
    val s1: Shape = new Rectangle(1, 2, "rectangle1")  
    val s2: Shape = Elipse(2, 3, "elipse1") // case class 会自动生成companion object，并生成apply()方法，所以可以不使用new
    val s3: Shape = Triangle(3, 4, 5, "triangle1") // 等效于Triangle.apply(side1, side2, side3, name)
    
    val shapes = List(s1, s2, s3)
    shapes.foreach { s => printf("%s area = %s\n", s.shapeType, s.area) }
    
    // 对case class 使用pattern match
    val shapes2 = List(
      Rectangle(2, 3, "r1"),
      Elipse(3, 4, "e1"),
      Triangle(3, 4, 5, "t1")
    )
    shapes2.foreach { s => s match {
      case Rectangle(_, _, name) => printf("rectangle name: %s\n", name)
      case Elipse(_, _, name) => printf("elipse name: %s\n", name)
      case Triangle(_, _, _, name) => printf("triangle name: %s\n", name)
      }
    }
    
    
  }
}