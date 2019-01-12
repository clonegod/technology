object Sqrt {
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
  
  def abs(n: Double):Double = if(n > 0) n else -n //> abs: (n: Double)Double
  
  // 递归
  def sqrt03(x: Double, y:Double):Double = {
    if(abs(y*y - x) > 1e-9) {
    		var tmp = (y + x/y) / 2;
    		println(tmp)
        return sqrt03(x, tmp);
    }
    return y;
  }                                               //> sqrt03: (x: Double, y: Double)Double
  
  sqrt03(2, 1.0d)                                 //> 1.5
                                                  //| 1.4166666666666665
                                                  //| 1.4142156862745097
                                                  //| 1.4142135623746899
                                                  //| res0: Double = 1.4142135623746899
  sqrt03(4, 1.0d)                                 //> 2.5
                                                  //| 2.05
                                                  //| 2.000609756097561
                                                  //| 2.0000000929222947
                                                  //| 2.000000000000002
                                                  //| res1: Double = 2.000000000000002
}