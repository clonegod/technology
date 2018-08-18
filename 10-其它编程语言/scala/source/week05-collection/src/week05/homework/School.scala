package week05.homework

class School {
  import scala.collection.mutable.Map
  var sMap:Map[Int, List[String]] = Map() 
  
  def add(name: String, grade: Int) = {
    if(sMap.contains(grade)) {
      val newList = name :: sMap.get(grade).get
      sMap += (grade -> newList)
    }
    else sMap += (grade -> List(name))
  }
  
  def del(name: String) = {
    sMap.keys.foreach { g => 
      if(sMap.get(g).exists { x => x.contains(name) }) {
    	  val newList = sMap.get(g).get.dropWhile { x => x==name }
    	  sMap += (g -> newList)
      }
    }
  }
  
  def update(name: String, grade: Int) = {
    del(name)
    add(name, grade)
  }
  
  def findByName(name: String): Tuple2[String, Int] = {
    for(g <- sMap.keySet)
      if(sMap.get(g).exists { x => x.contains(name) }) return (name, g)
    ("Not Exist", -1)
  }
  
  def findStusByGrade(grade: Int): List[String] = {
    sMap.get(grade) get
  }
  
  def displayStusByGrade(g:Array[Int]) {
    g.foreach { x =>  
      val stus = findStusByGrade(x)
      println("stu in grade " + x + ": " + stus)
    }
    println("---------------------------")
  }
  
}

object School {
  def main(args: Array[String]) {
    val sc = new School()
    
    println("添加学生和所在年级")
    sc.add("Alice", 1);
    sc.add("Annie", 1);
    sc.add("Bob", 2);
    sc.add("Cindy", 3);
    
    sc.displayStusByGrade(Array(1,2,3));
    
    println("修改Annie所在的年级")
    sc.update("Annie", 3)
    sc.displayStusByGrade(Array(1,3));
    
    println("查询Bob所在的年级")
    val stuInfo = sc.findByName("Bob")
    println(stuInfo)
    println("---------------------------")
    
    println("删除Annie")
    sc.del("Annie")
    sc.displayStusByGrade(Array(3));
    
  }
  
  
}