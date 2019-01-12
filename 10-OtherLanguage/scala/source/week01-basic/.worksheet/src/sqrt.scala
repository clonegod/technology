object Sqrt {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(346); 
/*
	// 调用库函数
  def sqrt01(x: Double):Double = {
		 Math.sqrt(x)
	}
	
  sqrt01(2)
  sqrt01(4)
	
	// 牛顿法求一个数的平方根
  def sqrt02(x: Double):Double = {
		var k:Double=1.0
    while(abs(k*k - x) > 1e-9) {
        k=(k + x/k) / 2;
    }
    return k;
  }
  
  sqrt02(2)
  sqrt02(4)
  */
  
  def abs(n: Double):Double = if(n > 0) n else -n;System.out.println("""abs: (n: Double)Double""");$skip(191); 
  
  // 递归
  def sqrt03(x: Double, y:Double):Double = {
    if(abs(y*y - x) > 1e-9) {
    		var tmp = (y + x/y) / 2;
    		println(tmp)
        return sqrt03(x, tmp);
    }
    return y;
  };System.out.println("""sqrt03: (x: Double, y: Double)Double""");$skip(22); val res$0 = 
  
  sqrt03(2, 1.0d);System.out.println("""res0: Double = """ + $show(res$0));$skip(18); val res$1 = 
  sqrt03(4, 1.0d);System.out.println("""res1: Double = """ + $show(res$1))}
}
