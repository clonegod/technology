object work02 {
 // println("Welcome to the Scala worksheet")
  
  
  class Person(n: String, a: Int) {
  	def name = n
  	def age = a
  }
	
	object Person {
		def apply(name: String, age: Int) = {
			new Person(name, age)
		}
	}
	
	val persons = List[Person](
		Person("alice", 18), Person("bob", 19),
		Person("jimi", 20), Person("mike", 21),
		Person("scott", 22), Person("tom", 23)
	)                                         //> persons  : List[work02.Person] = List(work02$Person@e5297a7, work02$Person@5
                                                  //| 0625c49, work02$Person@6998e5d9, work02$Person@351a3fb8, work02$Person@4e4d6
                                                  //| 444, work02$Person@3af1d485)
	
	// p => p.age > 20 lambda表达式，相当于往filter传入一个函数，只是这个函数通过lambda表示进行了简化
	persons.filter(p => p.age > 20).map(p => p.name)
                                                  //> res0: List[String] = List(mike, scott, tom)
	// 通过for循环进行过滤
	for(p <- persons if p.age > 20) yield p.name
                                                  //> res1: List[String] = List(mike, scott, tom)
	
	// 声明一个暂未确定具体实现的函数，可以用???来表示
	def isPrime(n: Int):Boolean = ???         //> isPrime: (n: Int)Boolean
	
	def checkPrime(n: Int) = {
		for { i <- 1 to n
					j <- 1 to i
					if isPrime(i + j)
		} yield (i, j)
	}                                         //> checkPrime: (n: Int)scala.collection.immutable.IndexedSeq[(Int, Int)]
	
}