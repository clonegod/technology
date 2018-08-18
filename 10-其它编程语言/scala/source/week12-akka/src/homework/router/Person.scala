package homework.router

object Teacher {
  def fromStr(line: String): Teacher = {
     val arr = line.split(",")
    new Teacher(new Integer(arr(0)),new String(arr(1)))
  }
}

class Teacher(val id:Int, val name:String) {
  override def equals(other: Any):Boolean = this.id == (other.asInstanceOf[Teacher]).id
  override def toString:String = id+","+name
}

object Student {
  def fromStr(line: String): Student = {
    val arr = line.split(",")
    new Student(new Integer(arr(0)),new String(arr(1)),new java.lang.Double(arr(2)))
  }
}

class Student(val id:Int, val name:String, val score:Double) {
  override def equals(other: Any):Boolean = this.id == (other.asInstanceOf[Student]).id
  override def toString:String = id+","+name+","+score
}

