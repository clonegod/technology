object Hello {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  println("hello world")                          //> hello world

	// 函数定义:无返回值
	def hello(): Unit = println("Hello scala");
                                                  //> hello: ()Unit
	hello()                                   //> Hello scala
	
	// 函数定义：简单函数可一行书写
	def abs(x: Int) = if (x > 0) x else -x    //> abs: (x: Int)Int
  abs(-2)                                         //> res0: Int = 2
  abs(0)                                          //> res1: Int = 0
  abs(2)                                          //> res2: Int = 2
	
	// 函数定义
	def max(x: Int, y: Int): Int = {
		if(x > y)
			x
		else
			y
	}                                         //> max: (x: Int, y: Int)Int
	
	max(3, 5)                                 //> res3: Int = 5
	
	// 不可变量
  val d = 1 + 2                                   //> d  : Int = 3
  d                                               //> res4: Int = 3
  
  // 可变量
  var x: Int = 1                                  //> x  : Int = 1
  x = 3	//statement无法评估值，因此没有输出
  x                                               //> res5: Int = 3
  
  // 数组: 数组本身不可变（长度，类型不可变），但是内部元素的值可变
  val arr = new Array[String](3)                  //> arr  : Array[String] = Array(null, null, null)
 	arr(0) = "a"
 	arr(1) = "b"
 	arr(2) = "c"
 	arr                                       //> res6: Array[String] = Array(a, b, c)
 	
 	// def定义的变量
 	def y = 1                                 //> y: => Int
 	y                                         //> res7: Int = 1
 	// def定义的变量只有在使用的时候才进行评估，即使定义本身有错，如果没有具体使用变量值，也不会报错
 	def loop:Boolean = loop                   //> loop: => Boolean
 	def x2 = loop                             //> x2: => Boolean
 	
 	
 	// 计算斐波拉契数列中第n个数的值
 	// 斐波拉契数列: 1,1,2,3,5,8
 	def fib(n: Int): Int = {
 		if(n <=0 || n == 1 || n == 2)
 			1
 		else
 			fib(n-2) + fib(n-1)
 	}                                         //> fib: (n: Int)Int
 	
 	fib(1)                                    //> res8: Int = 1
 	fib(2)                                    //> res9: Int = 1
 	fib(3)                                    //> res10: Int = 2
 	fib(4)                                    //> res11: Int = 3
 	fib(5)                                    //> res12: Int = 5
 	fib(6)                                    //> res13: Int = 8
 	fib(30)                                   //> res14: Int = 832040
}