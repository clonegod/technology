

object ListDemo extends App {
  
  val a = "a" :: "b" :: Nil
  println(a)
  println(a.apply(0))
  println(a.apply(0)=="a")
  
  val b = List("c", "d") ::: a
  println(b)
  
  b match {
    case Nil =>
      println("Empty list")
    case h::t =>
      print("head is ")
      println(h)
      print("tail is ")
      println(t)
  }
  
  a.foreach { x => println(x) }
}