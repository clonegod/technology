package week02.homework

/**
 * 计算杨辉三角任意位置的元素值
 */
class Triangles {
  
  def pascal(row: Int, col: Int): Int = {
    
    if(row < 1 || col < 1 || row < col) {
    	println("Illegal row or col, please check")
    	-1 // error
    }
    
    // 每行第1个元素、最后一个元素都是1（每行第1个元素：col == 1，每行最后1个元素：row == col）
    if(col == 1 || row == col)
      1
    else
      // 上一行: row-1，前一个元素: col-1，相同位置的元素：col
      pascal(row-1, col-1) + pascal(row-1, col)
    
  }
  
}

object Triangles extends App {
	val row = 6; val col = 4;
	
  val instance = new Triangles()
  
  val value = instance.pascal(row, col)
  
  println("第"+row+"行第"+col+"列，元素值为："+value)
}