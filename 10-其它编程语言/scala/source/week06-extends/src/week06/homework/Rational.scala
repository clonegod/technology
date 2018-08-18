package week06.homework

/**
 * 一个有理数(rational)可以表示成个分数形式： n/d, 其中n和d都是整数（d不可以为0），n称为分子(numberator)，d为分母(denominator)。
 * 
 * 类参数和构造函数，方法，操作符，私有成员，重载
 * 
 * @author clonegod
 */
class Rational(val n:Int, val d:Int) {
  
  /**前提条件检查*/
  require(d!=0)
  
  import Rational._
  /**定义成员变量*/
  private val g = gcd (n.abs, d.abs) // 私有变量g来保存最大公倍数
  
  val numerator = n/g // 分子
  
  val denominator = d/g  // 分母
   
  /**
   * 辅助构造函数
   */
  def this(n:Int) = this(n,1)
  
  
  /**加法*/
  def +(that:Rational) : Rational = {
	  new Rational(
			  numerator * that.denominator + that.numerator* denominator,
			  denominator * that.denominator 
		)
  }
  
  def + (i:Int) = {
	  new Rational (numerator + i * denominator, denominator)
  }
  
  
  /**减法*/
  def -(that:Rational) : Rational = {
	  new Rational(
			  numerator * that.denominator - that.numerator* denominator,
			  denominator * that.denominator 
		)
  }
  
  def - (i:Int) = {
	  new Rational (numerator - (i * denominator), denominator)
  }
  
  
  /**乘法*/
  def * (that:Rational) = {
	  new Rational( 
			  numerator * that.numerator, 
			  denominator * that.denominator)
  }
  
  def * (i:Int) = {
	  new Rational (numerator * i, denominator)
  }
  
  
  /**除法*/
  def / (that:Rational) = {
	  new Rational( 
			  numerator * that.denominator, 
			  denominator * that.numerator)
  }
  
  def / (i:Int) = {
	  new Rational (numerator, i * denominator)
  }
  
  
  /** 重新定义类的toString 方法 */
  override def toString = "(" + n + "/" + d + ")"
  
}

object Rational {
  /**
   * 私有成员方法
   * 获取最大公约数，用于化简分数-回溯函数，必须定义返回值类型
   */
  private def gcd(a:Int,b:Int):Int = {
    if(b==0) a 
    else gcd(b, a % b)
  }
  
  /**
   * 隐式类型转换，让Int类型能够根据需要自动转换为Rational类型
   * */
  implicit def intToRational(x:Int) = new Rational(x)
  
  def execute(symbol:Char, r1:Rational, r2:Rational): Unit = {
    symbol match {
      case '+' => printf("%s + %s = %s \n", r1, r2, (r1 + r2))
      case '-' => printf("%s - %s = %s \n", r1, r2, (r1 - r2))
      case '*' => printf("%s * %s = %s \n", r1, r2, (r1 * r2))
      case '/' => printf("%s / %s = %s \n", r1, r2, (r1 / r2))
    }
  }
  
  def main(args: Array[String]) {
    
    println("\n<============分数的加减乘除计算========>")
    execute('+', new Rational(1,2), new Rational(1,3))
    execute('-', new Rational(1,2), new Rational(1,3))
    execute('*', new Rational(1,2), new Rational(1,3))
    execute('/', new Rational(1,2), new Rational(1,3))
    
    println("\n<============求最大公约数==========>")
    println("36和48的最大公约数：" + gcd (36, 48))
    
    println("\n<============分数对象和整数对象的加减乘除操作==========>")
    execute('+', new Rational(1,2), 2)
    execute('+', 2, new Rational(1,2))
    
    execute('-', new Rational(1,2), 2)
    execute('-', 2, new Rational(1,2))
    
    execute('*', new Rational(1,2), 2)
    execute('*', 2, new Rational(1,2))
    
    execute('/', new Rational(1,2), 2)
    execute('/', 2 , new Rational(1,2))
    
  }
}