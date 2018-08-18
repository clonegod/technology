package sample.mixin

class StringIterator(s: String) extends AbsIterator {
  type T = Char
  
  private var i = 0
  
  def hasNext = i < s.length()
  
  def next = { val ch = s charAt i; i += 1; ch }
  
}

object StringIteratorTest {
  def main(args: Array[String]) {
    val str = "hello"
    class Iter extends StringIterator(str) with RichIterator
    val iter = new Iter
    iter foreach println
  }
}