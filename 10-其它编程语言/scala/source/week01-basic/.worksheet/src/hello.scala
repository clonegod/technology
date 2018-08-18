object Hello {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(59); 

  println("Welcome to the Scala worksheet");$skip(25); 
  println("hello world");$skip(60); 

	// 函数定义:无返回值
	def hello(): Unit = println("Hello scala");System.out.println("""hello: ()Unit""");$skip(9); ;
	hello();$skip(61); 
	
	// 函数定义：简单函数可一行书写
	def abs(x: Int) = if (x > 0) x else -x;System.out.println("""abs: (x: Int)Int""");$skip(10); val res$0 = 
  abs(-2);System.out.println("""res0: Int = """ + $show(res$0));$skip(9); val res$1 = 
  abs(0);System.out.println("""res1: Int = """ + $show(res$1));$skip(9); val res$2 = 
  abs(2);System.out.println("""res2: Int = """ + $show(res$2));$skip(77); 
	
	// 函数定义
	def max(x: Int, y: Int): Int = {
		if(x > y)
			x
		else
			y
	};System.out.println("""max: (x: Int, y: Int)Int""");$skip(13); val res$3 = 
	
	max(3, 5);System.out.println("""res3: Int = """ + $show(res$3));$skip(27); 
	
	// 不可变量
  val d = 1 + 2;System.out.println("""d  : Int = """ + $show(d ));$skip(4); val res$4 = 
  d;System.out.println("""res4: Int = """ + $show(res$4));$skip(29); 
  
  // 可变量
  var x: Int = 1;System.out.println("""x  : Int = """ + $show(x ));$skip(32); 
  x = 3;$skip(4); val res$5 = 	//statement无法评估值，因此没有输出
  x;System.out.println("""res5: Int = """ + $show(res$5));$skip(74); 
  
  // 数组: 数组本身不可变（长度，类型不可变），但是内部元素的值可变
  val arr = new Array[String](3);System.out.println("""arr  : Array[String] = """ + $show(arr ));$skip(15); 
 	arr(0) = "a";$skip(15); 
 	arr(1) = "b";$skip(15); 
 	arr(2) = "c";$skip(6); val res$6 = 
 	arr;System.out.println("""res6: Array[String] = """ + $show(res$6));$skip(29); 
 	
 	// def定义的变量
 	def y = 1;System.out.println("""y: => Int""");$skip(4); val res$7 = 
 	y;System.out.println("""res7: Int = """ + $show(res$7));$skip(80); 
 	// def定义的变量只有在使用的时候才进行评估，即使定义本身有错，如果没有具体使用变量值，也不会报错
 	def loop:Boolean = loop;System.out.println("""loop: => Boolean""");$skip(16); 
 	def x2 = loop;System.out.println("""x2: => Boolean""");$skip(155); 
 	
 	
 	// 计算斐波拉契数列中第n个数的值
 	// 斐波拉契数列: 1,1,2,3,5,8
 	def fib(n: Int): Int = {
 		if(n <=0 || n == 1 || n == 2)
 			1
 		else
 			fib(n-2) + fib(n-1)
 	};System.out.println("""fib: (n: Int)Int""");$skip(12); val res$8 = 
 	
 	fib(1);System.out.println("""res8: Int = """ + $show(res$8));$skip(9); val res$9 = 
 	fib(2);System.out.println("""res9: Int = """ + $show(res$9));$skip(9); val res$10 = 
 	fib(3);System.out.println("""res10: Int = """ + $show(res$10));$skip(9); val res$11 = 
 	fib(4);System.out.println("""res11: Int = """ + $show(res$11));$skip(9); val res$12 = 
 	fib(5);System.out.println("""res12: Int = """ + $show(res$12));$skip(9); val res$13 = 
 	fib(6);System.out.println("""res13: Int = """ + $show(res$13));$skip(10); val res$14 = 
 	fib(30);System.out.println("""res14: Int = """ + $show(res$14))}
}
