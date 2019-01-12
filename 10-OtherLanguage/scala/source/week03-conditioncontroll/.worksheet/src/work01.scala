object work01 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(36); 

  val r1 = 1 to 10;System.out.println("""r1  : scala.collection.immutable.Range.Inclusive = """ + $show(r1 ));$skip(24); 

  val r2 = 1 until 10;System.out.println("""r2  : scala.collection.immutable.Range = """ + $show(r2 ));$skip(39); 

  for (i <- 1 to 5) {
    println(i)
  };$skip(32); 


	val L1 = List(1,2,3,4,5);System.out.println("""L1  : List[Int] = """ + $show(L1 ));$skip(73); val res$0 = 
	
	// 对集合中的元素进行迭代，通过yield对每个元素进行操作-generator 生成器
	for(x <- L1) yield x*x;System.out.println("""res0: List[Int] = """ + $show(res$0));$skip(58); val res$1 = 

	// 对集合中符合条件的元素进行处理
	for(x <- L1 if (x%2==0) ) yield x*x;System.out.println("""res1: List[Int] = """ + $show(res$1))}
	
	
}
