object FlatMap {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(60); 
  println("Welcome to the Scala worksheet");$skip(149); 
  
  def toInt(s: String): Option[Int] = {
  
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  };System.out.println("""toInt: (s: String)Option[Int]""");$skip(52); 
  
  val strings = Seq("1", "2", "foo", "3", "bar");System.out.println("""strings  : Seq[String] = """ + $show(strings ));$skip(21); val res$0 = 
  strings.map(toInt);System.out.println("""res0: Seq[Option[Int]] = """ + $show(res$0));$skip(25); val res$1 = 
  strings.flatMap(toInt);System.out.println("""res1: Seq[Int] = """ + $show(res$1));$skip(29); val res$2 = 
  strings.flatMap(toInt).sum;System.out.println("""res2: Int = """ + $show(res$2));$skip(34); 
  
  val map = strings.map(toInt);System.out.println("""map  : Seq[Option[Int]] = """ + $show(map ));$skip(24); 
  val res = map.flatten;System.out.println("""res  : Seq[Int] = """ + $show(res ));$skip(10); val res$3 = 
  res.sum;System.out.println("""res3: Int = """ + $show(res$3))}
}
