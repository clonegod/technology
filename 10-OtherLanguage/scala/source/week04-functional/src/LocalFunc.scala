

class LocalFunc {
  private def newton(x: Double, guess: Double): Double = {
    // 定义内部函数，可避免与外部函数名冲突的问题
    def isGoodEnough(sqrt: Double, expected: Double) = {
      
      math.abs(expected - sqrt * sqrt) < 1.0E-10
    }
    
    if (isGoodEnough(guess, x)) guess
    else newton(x, (guess + x / guess) / 2)
  }
  
  def sqrt(x: Double) = newton(x, 1)
}

object LocalFunc extends App {
  
  val l = new LocalFunc() 
  println(l.sqrt(2))
  println(l.sqrt(4))
}