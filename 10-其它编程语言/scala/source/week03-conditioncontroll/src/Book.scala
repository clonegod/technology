class Book(t: String, a: List[String]) {
  val title = t
  val authors = a
  
  // 判断是否为同一本书：书名相同&&作者相同
  def ==(book: Book) = {
    title == book.title && authors == book.authors
  }
}



object Book {
   
   // apply用来提供创建对象的简单方法-避免每次创建对象都需要写new
   def apply(t: String, a: List[String]) = {
     new Book(t, a)
   }
   
   // 查找写了2本书的作者
   
   def find1(books: List[Book]) = {
     for{b1 <- books
         b2 <- books
         if b1.title != b2.title // 书名不同
         a1 <- b1.authors
         a2 <- b2.authors
         if a1 == a2 // 作者相同
     } yield a1
   }
   
   def find2(books: List[Book]) = {
     for{b1 <- books
         b2 <- books
         if b1.title < b2.title // 规定了每次比较的顺序，都用小的与大的比较，这样就避免了相互比较2次的问题
         a1 <- b1.authors
         a2 <- b2.authors
         if a1 == a2 // 作者相同
     } yield a1
   }
   
   def find3(books: List[Book]) = {
	   (for{b1 <- books
		   b2 <- books
		   if b1.title != b2.title // 书名不同
		   a1 <- b1.authors
		   a2 <- b2.authors
		   if a1 == a2 // 作者相同
	   } yield a1).distinct // 对结果进行去重
   }
   
   
   def find4(books: List[Book]) = {
     
	   // List转换为Set，这样就很好的利用了Set元素不重复的特性  
     def booksSet = books.toSet
     
     // 由于是对Set的遍历，所以结果也将存入同样的Set数据结构中，保证了结果不会有重复元素，避免了再次进行去重的操作
     for{b1 <- booksSet
		   b2 <- booksSet
		   if b1.title != b2.title // 书名不同
		   a1 <- b1.authors
		   a2 <- b2.authors
		   if a1 == a2 // 作者相同
	   } yield a1
   }
   
   // 由于object Book 没有继承App，所以需要手动写main函数
   // 注意，main函数的参数为Array[String]， 不是List[String]
   def main(args: Array[String]) = {
     
     // 初始化book集合
     val books = List[Book](
       Book("java",List("A1", "A11")),
       Book("scala",List("A2", "A9")),
       Book("jvm",List("A3")),
       Book("concurrent",List("A1", "A2")),
       Book("shell",List("A4", "A5"))
     )
     
     var a1 = find1(books)
     print("a1: "); println(a1)
     
     var a2 = find2(books)
     print("a2: "); println(a2)
     
     var a3 = find3(books)
     print("a3: "); println(a3)
     
     var a4 = find4(books)
     print("a4: "); println(a4)
     
     
   }
   
  
}