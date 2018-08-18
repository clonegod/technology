package homework10

class Teacher(val id:Int, val name:String) {
  override def equals(other: Any):Boolean = this.id == (other.asInstanceOf[Teacher]).id
  override def toString:String = id+","+name
}

class Student(val id:Int, val name:String, val score:Double) {
  override def equals(other: Any):Boolean = this.id == (other.asInstanceOf[Student]).id
  override def toString:String = id+","+name+","+score
}

