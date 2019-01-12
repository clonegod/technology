object work01 {

  val r1 = 1 to 10                                //> r1  : scala.collection.immutable.Range.Inclusive = Range(1, 2, 3, 4, 5, 6, 7,
                                                  //|  8, 9, 10)

  val r2 = 1 until 10                             //> r2  : scala.collection.immutable.Range = Range(1, 2, 3, 4, 5, 6, 7, 8, 9)

  for (i <- 1 to 5) {
    println(i)                                    //> 1
                                                  //| 2
                                                  //| 3
                                                  //| 4
                                                  //| 5
  }


	val L1 = List(1,2,3,4,5)                  //> L1  : List[Int] = List(1, 2, 3, 4, 5)
	
	// 对集合中的元素进行迭代，通过yield对每个元素进行操作-generator 生成器
	for(x <- L1) yield x*x                    //> res0: List[Int] = List(1, 4, 9, 16, 25)

	// 对集合中符合条件的元素进行处理
	for(x <- L1 if (x%2==0) ) yield x*x       //> res1: List[Int] = List(4, 16)
	
	
}