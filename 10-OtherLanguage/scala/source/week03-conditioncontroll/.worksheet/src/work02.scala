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
	};import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(388); 
	
	val persons = List[Person](
		Person("alice", 18), Person("bob", 19),
		Person("jimi", 20), Person("mike", 21),
		Person("scott", 22), Person("tom", 23)
	);System.out.println("""persons  : List[work02.Person] = """ + $show(persons ));$skip(121); val res$0 = 
	
	// p => p.age > 20 lambda表达式，相当于往filter传入一个函数，只是这个函数通过lambda表示进行了简化
	persons.filter(p => p.age > 20).map(p => p.name);System.out.println("""res0: List[String] = """ + $show(res$0));$skip(62); val res$1 = 
	// 通过for循环进行过滤
	for(p <- persons if p.age > 20) yield p.name;System.out.println("""res1: List[String] = """ + $show(res$1));$skip(67); 
	
	// 声明一个暂未确定具体实现的函数，可以用???来表示
	def isPrime(n: Int):Boolean = ???;System.out.println("""isPrime: (n: Int)Boolean""");$skip(110); 
	
	def checkPrime(n: Int) = {
		for { i <- 1 to n
					j <- 1 to i
					if isPrime(i + j)
		} yield (i, j)
	};System.out.println("""checkPrime: (n: Int)scala.collection.immutable.IndexedSeq[(Int, Int)]""")}
	
}
