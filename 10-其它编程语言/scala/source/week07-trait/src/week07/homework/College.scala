package week07.homework

import scala.math.Ordering

class College {
  
  import scala.collection.mutable.Map
  
  // key为年级，value为老师和学生的集合
  val allPerson:Map[Int, List[CollegePerson]] = Map()
  
  /**
   * 添加学生或老师
   * @p 被添加对象
   * @g 年级
   */
  def add(p: CollegePerson, g: Int) = {
    if(allPerson.contains(g)) {
      val newList = p :: allPerson.get(g).get
      allPerson += (g -> newList)
    }
    else allPerson += (g -> List(p))
  }
  
  /**
   * 查询所有的老师或学生
   * @g 年级
   * @ord 排序规则
   * @cls 目标类型
   */
  def listAll(g: Int, ord:Ordering[CollegePerson], cls:Class[_]):Unit = {
  	val or:Option[List[CollegePerson]] = allPerson.get(g)
  	if(or == None) 	print("Not Found")
  	else  or.get.filter { p => p.getClass == cls }.sorted(ord).foreach { println }
  }
  
  /**
   * 删除老师或学生
   * @p 被删除对象
   */
  def del(p: CollegePerson, g: Int) = {
    val or:Option[List[CollegePerson]] = allPerson.get(g)
    if(or != None) {
      val iter:Iterator[CollegePerson]  = or.get.iterator
      val newList = iter.filterNot { x => x.name==p.name && x.age==p.age }.toList
      allPerson += (g -> newList)
    }
  }
  
  /**
   * 更新老师或学生的信息
   * @p 被更新对象
   * @grade 年级
   */
  def update(p: CollegePerson, old_g:Int, new_g: Int):Unit = {
    del(p, old_g)
    add(p, new_g)
  }
  
  // 学生排序规则：按分数降序排
	val stuOrdByScoreDesc = new Ordering[CollegePerson] {
		override def compare(p1: CollegePerson, p2: CollegePerson): Int = {
				(p2.asInstanceOf[Student].score - p1.asInstanceOf[Student].score).toInt
		}
	}
	
	// 老师排序规则：按年龄升序排
	val teaOrdByAgeAsc = new Ordering[CollegePerson] {
		override def compare(p1: CollegePerson, p2: CollegePerson): Int = p1.age - p2.age
	}
  
  def reportAllStu() = {
      allPerson.keys.toList.sortWith((a, b) => a < b).foreach { g => 
        println("-----------All student in grade " + g + " sort by score desc-----------")
        listAll(g, stuOrdByScoreDesc, classOf[Student])
        println()
      }
  }
  
  def reportAllTeacher() = {
  	allPerson.keys.toList.sortWith((a, b) => a < b).foreach { g => 
        println("-----------All Teacher in grade " + g + " sort by age asc-----------")
        listAll(g, teaOrdByAgeAsc, classOf[Teacher])
        println()
      }
  }
  
}

object College extends App {
	
  val college = new College()
  
  // 添加学生
  college.add(new Student("mike", 6, 90), 1)
  college.add(new Student("sam", 7, 80), 1)
  college.add(new Student("tom", 6, 87), 1)
  college.add(new Student("stevin", 8, 98), 1)
  college.add(new Student("bob", 5, 78), 1)
  college.add(new Student("alice", 5, 99), 1)
  college.reportAllStu()
  
  // 添加老师
  college.add(new Teacher("sandy", 35), 1)
  college.add(new Teacher("pujin", 42), 1)
  college.add(new Teacher("obama", 28), 1)
  college.reportAllTeacher()
  
  //更新、删除学生
  college.del(new Student("alice", 5, 99), 1)
  college.update(new Student("bob", 5, 100), 1, 2)
  println("\n-----------delete alice-----------")
  println("-----------update bob's grade from 1 to 2-----------")
  college.reportAllStu()

  //更新老师
  college.update(new Teacher("sandy", 35), 1, 2)
  println("\n-----------update sandy's grade from 1 to 2-----------")
  college.reportAllTeacher()
  
}