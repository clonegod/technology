package week08.homework

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.math.Ordering

class College {

  import scala.collection.mutable.Map

  // key为年级，value为老师和学生的集合
  val allPerson: Map[Int, List[CollegePerson]] = Map()

  // 读写锁：没有写操作时，读是无阻塞的
  val lock: ReadWriteLock = new ReentrantReadWriteLock()
  val readLock = lock.readLock();
  val writeLock = lock.writeLock();

  /**
   * 添加学生或老师
   * @p 被添加对象
   * @g 年级
   */
  def add(p: CollegePerson, g: Int): Future[Boolean] = {
    Future {
      try {
        writeLock.lock()
        if (allPerson.contains(g)) {
          val newList = p :: allPerson.get(g).get
          allPerson += (g -> newList)
        } else allPerson += (g -> List(p))
        true
      }
      finally { writeLock.unlock() }
    }
  }

  /**
   * 删除老师或学生
   * @p 被删除对象
   */
  def del(p: CollegePerson, g: Int): Future[Boolean] = {
    Future {
      try {
        writeLock.lock()
        val or: Option[List[CollegePerson]] = allPerson.get(g)
        if (or != None) {
          val iter: Iterator[CollegePerson] = or.get.iterator
          val newList = iter.filterNot { x => x.name == p.name && x.age == p.age }.toList
          allPerson += (g -> newList)
        }
        true
      }
      finally { writeLock.unlock() }
    }
  }

  /**
   * 更新老师或学生的信息
   * @p 被更新对象
   * @grade 年级
   */
  def update(p: CollegePerson, old_g: Int, new_g: Int): Future[Boolean] = {
    Future {
      try {
        writeLock.lock()
        del(p, old_g)
        add(p, new_g)
        true
      }
      finally { writeLock.unlock() }
    }
  }

  def reportAllStu(g: Int): Future[List[CollegePerson]] = {
    Future {
      try {
        readLock.lock()
        listAll(g, stuOrdByScoreDesc, classOf[Student])
      }
      finally { readLock.unlock() }
    }
  }

  def reportAllTeacher(g: Int): Future[List[CollegePerson]] = {
    Future {
      try {
        readLock.lock()
        listAll(g, teaOrdByAgeAsc, classOf[Teacher])
      }
      finally { readLock.unlock() }
    }
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

  /**
   * 查询所有的老师或学生
   * @g 年级
   * @ord 排序规则
   * @cls 目标类型
   */
  def listAll(g: Int, ord: Ordering[CollegePerson], cls: Class[_]): List[CollegePerson] = {
    val or: Option[List[CollegePerson]] = allPerson.get(g)
    if (or == None) List[CollegePerson]()
    else or.get.filter { p => p.getClass == cls }.sorted(ord)
  }

}
