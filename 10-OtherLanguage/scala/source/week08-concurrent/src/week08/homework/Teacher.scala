package week08.homework

class Teacher(override val name: String, override val age: Int) 
  extends CollegePerson(name, age) {
    
  override def toString:String = "name="+name+", age="+age

  
}