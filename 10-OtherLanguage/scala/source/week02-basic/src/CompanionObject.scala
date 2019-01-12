

class CompanionObject(n: String) {
  
  // 私有成员变量
  private val name = n
  
  // 辅助构造函数
  def this() {
    this("abc")
  }
  
  def show() = println(name)
  
  def showList() = println(name.toList)
  
  // _ 表示引入该类的所有成员
  import CompanionObject._
  
  def getValue(key: Char) = dictionary.getOrElse(key, "0") // 如果对于的key不存在，则返回默认值'0'
  
  // pattern match 结合递归完成字符串的加密转换
  def merge(content: List[Char], result: List[String]): List[String] = {
    content match {
      case Nil => result
      case head::tail => {
        print("head is "); println(head)          
        print("tail is "); println(tail)          
        val left = result :+ getValue(head) // 往result list末尾追加元素
        print("left is "); println(left)
        merge(tail, left) 
      }
    }
  }
  
  // 对list中的字符进行转换
  def translate(): List[String] = {
    val chars = name.toList
    merge(chars, List())
  }
}

object CompanionObject {
  
  // companion object提供互相访问对方成员的功能，包括私有属性
  private val dictionary = Map('a' -> "1", 'b' -> "2", 'c' -> "3")
  
  // apply包装了对CompanionObject类的构造函数的调用，对外提供另一种访问方式
  def apply(n: String) = {
    new CompanionObject(n)
  }
  
  // apply包装了对CompanionObject类的构造函数的调用，对外提供另一种访问方式：传入参数可以是Int类型
  def apply(i: Int) = {
    new CompanionObject()
  }
  
  def main(args: Array[String]) = {

    val obj = CompanionObject(1)
    obj.show()
    obj.showList()
    
    val l = obj.translate()
    println(l)
  }
}