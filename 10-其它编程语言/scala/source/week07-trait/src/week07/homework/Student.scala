package week07.homework

class Student (override val name: String, override val age: Int, val score: Double) 
  extends CollegePerson(name, age) {
  
  override def toString:String = "name="+name+", age="+age+", score="+score
}