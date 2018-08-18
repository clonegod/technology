package week08.homework

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success

object Test extends App {

  val college = new College

  printf("\n====================Init student and teacher=====================\n")
  college.add(new Student("alice", 5, 99), 1)
  college.add(new Student("mike", 6, 90), 1)
  college.add(new Student("tom", 6, 87), 1)
  college.add(new Student("bob", 5, 78), 1)
  college.add(new Student("sam", 7, 80), 2)
  
  college.add(new Teacher("t_pujin", 42), 1)
  college.add(new Teacher("t_obama", 28), 1)
  college.add(new Teacher("t_sandy", 35), 2)
  
  TimeUnit.SECONDS.sleep(3)
  
  printf("\n====================Print student and teacher=====================\n")
  college.reportAllStu(1).onComplete {
    case Success(stus) => stus.foreach { stu => printf("grade=%d, stu:%s\n", 1, stu) }
    case Failure(ex)   => ex.printStackTrace 
  }

  TimeUnit.SECONDS.sleep(1)
  
  college.reportAllStu(2).onComplete {
    case Success(stus) => stus.foreach { stu => printf("grade=%d, stu:%s\n", 2, stu) }
    case Failure(ex)   => ex.printStackTrace
  }
  
  TimeUnit.SECONDS.sleep(1)

  college.reportAllTeacher(1).onComplete {
    case Success(teas) => teas.foreach { tea => printf("grade=%d, tea:%s\n", 1, tea) }
    case Failure(ex)   => ex.printStackTrace
  }
  
  TimeUnit.SECONDS.sleep(1)

  college.reportAllTeacher(2).onComplete {
    case Success(teas) => teas.foreach { tea => printf("grade=%d, tea:%s\n", 2, tea) }
    case Failure(ex)   => ex.printStackTrace
  }
  
  TimeUnit.SECONDS.sleep(1)
  
  
  printf("\n====================并发CRUD=====================\n")
  
  college.add(new Student("stevin", 8, 98), 1).onComplete {
    case Success(b) => printf("%s, 新增学生stevin: %s\n", Thread.currentThread().getName, b)
    case Failure(ex) => ex.printStackTrace
  }

  college.del(new Student("alice", 5, 99), 1).onComplete {
    case Success(b) => printf("%s, 删除学生alice: %s\n", Thread.currentThread().getName, b)
    case Failure(ex) => ex.printStackTrace
  }

  college.update(new Student("tom", 6, 78), 1, 2).onComplete {
    case Success(b) => printf("%s, 更新学生tom: %s\n", Thread.currentThread().getName, b)
    case Failure(ex) => ex.printStackTrace
  }

  college.update(new Student("bob", 5, 100), 1, 2).onComplete {
    case Success(b) => printf("%s, 更新学生bob: %s\n", Thread.currentThread().getName, b)
    case Failure(ex) => ex.printStackTrace
  }

  college.update(new Teacher("t_obama", 28), 1, 2).onComplete {
    case Success(b) => printf("%s, 更新老师t_obama: %s\n", Thread.currentThread().getName, b)
    case Failure(ex) => ex.printStackTrace
  }

  TimeUnit.SECONDS.sleep(3)
  
  printf("\n===================验证并发后CRUD结果是否正确======================\n")

  college.reportAllStu(1).onComplete {
    case Success(stus) => stus.foreach { stu => printf("grade=%d, stu:%s\n", 1, stu) }
    case Failure(ex)   => ex.printStackTrace 
  }

  TimeUnit.SECONDS.sleep(1)
  
  college.reportAllStu(2).onComplete {
    case Success(stus) => stus.foreach { stu => printf("grade=%d, stu:%s\n", 2, stu) }
    case Failure(ex)   => ex.printStackTrace
  }
  
  TimeUnit.SECONDS.sleep(1)

  college.reportAllTeacher(1).onComplete {
    case Success(teas) => teas.foreach { tea => printf("grade=%d, tea:%s\n", 1, tea) }
    case Failure(ex)   => ex.printStackTrace
  }
  
  TimeUnit.SECONDS.sleep(1)

  college.reportAllTeacher(2).onComplete {
    case Success(teas) => teas.foreach { tea => printf("grade=%d, tea:%s\n", 2, tea) }
    case Failure(ex)   => ex.printStackTrace
  }

  TimeUnit.SECONDS.sleep(3)

}