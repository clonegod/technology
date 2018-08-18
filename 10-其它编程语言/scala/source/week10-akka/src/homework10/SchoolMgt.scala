package homework10

import akka.actor._
import akka.actor.SupervisorStrategy.{ Stop, Resume, Restart }
import scala.collection.mutable.ListBuffer
import scala.io.Source
import java.io.File
import java.io.PrintWriter
import java.util.concurrent.TimeUnit
import java.io.FileOutputStream

class StudentActor extends Actor {
  val dataPath = "/student.txt";
  var studentList = ListBuffer[Student]()
  
  println("StudentActor Constructor")
  
  override def preStart() = println("StudentActor pre-start")
  
  override def postStop() = println("StudentActor post-stop")
  
  override def preRestart(reason: Throwable, msg: Option[Any]) = {
    println("StudentActor pre-restart reason: " + reason.getMessage)
    super.preRestart(reason, msg)
  }
  
  /**恢复actor中的状态数据：加载本地文件中保存的数据，重建studentList*/
  override def postRestart(reason: Throwable) = {
	  // => reload studentList after restart
	  recoverData()
    println("StudentActor post-restart, studentList size=" + studentList.size)
    super.postRestart(reason)
  }
  
  def receive = {
    case msg:S_ADD =>
      println("StudentActor S_ADD received:"+msg.toString)
      add(msg.event)
    case msg:S_DELETE =>
      println("StudentActor S_DELETE received:"+msg.toString)
      delete(msg.event)
    case msg:S_UPDATE =>
      println("StudentActor S_UPDATE received:"+msg.toString)
      delete(msg.event)
      add(msg.event)
    case msg:Any => 
      throw new RuntimeException("StudentActor forced restart")
  }
    
  def add(s:Student):Unit = {
    studentList.append(s)
    saveData()
  }
	
  def delete(s:Student):Unit = {
    studentList = studentList.filterNot { student => student.id == s.id }
    saveData()
  }
  
  def saveData() = {
    val file = new File(dataPath)
    if(! file.exists()) file.createNewFile()
    val writer = new PrintWriter(new FileOutputStream(file, false /* append = false */))
    for(stu <- studentList) {
    	writer.write(stu.toString())
    	writer.write(System.getProperty("line.separator"))
    }
    writer.flush()
    writer.close()
  }
  
  def recoverData() = {
    val file = Source.fromFile(dataPath)
    for(line <- file.getLines)
    {
      val arr = line.split(",")
      studentList.append(new Student(new Integer(arr.head),new String(arr.head),new java.lang.Double(arr.head)))
    }
    file.close
  }
}

class TeacherActor extends Actor {
  
  val dataPath = "/teacher.txt";
  var teacherList = ListBuffer[Teacher]()
  
	println("TeacherActor Constructor")
	
	override def preStart() = println("TeacherActor pre-start")
	
	override def postStop() = println("TeacherActor post-stop")
	
	override def preRestart(reason: Throwable, msg: Option[Any]) = {
		println("TeacherActor pre-restart reason: " + reason.getMessage)
		super.preRestart(reason, msg)
	}
  
	/**恢复actor中的状态数据：加载本地文件中保存的数据，重建teacherList*/
	override def postRestart(reason: Throwable) = {
	  // => reload teacherList after restart
		recoverData()
		println("TeacherActor post-restart, teacherList size=" + teacherList.size)
		super.postRestart(reason)
	}
	
  def receive = {
    case msg:T_ADD =>
      println("TeacherActor T_ADD received:"+msg.toString)
      add(msg.event)
    case msg:T_DELETE =>
      println("TeacherActor T_DELETE received:"+msg.toString)
      delete(msg.event)
    case msg:T_UPDATE =>
      println("TeacherActor T_UPDATE received:"+msg.toString)
      delete(msg.event)
      add(msg.event)
    case msg:Any => 
      throw new RuntimeException("TeacherActor forced restart")
  }
  
  def add(t:Teacher):Unit = {
    teacherList.append(t)
    saveData()
  }
	
  def delete(t:Teacher):Unit = {
    teacherList = teacherList.filterNot { teacher => teacher.id == t.id }
    saveData()
  }
  
  def saveData() = {
    val file = new File(dataPath)
    if(! file.exists()) file.createNewFile()
    val writer = new PrintWriter(new FileOutputStream(file, false /* append = false */))
    for(tea <- teacherList) {
    	writer.write(tea.toString())
    	writer.write(System.getProperty("line.separator"))
    }
    writer.flush()
    writer.close()
  }
  
  def recoverData() = {
    val file = Source.fromFile(dataPath)
    for(line <- file.getLines)
    {
      val arr = line.split(",")
      teacherList.append(new Teacher(new Integer(arr.head),new String(arr.head)))
    }
    file.close
  }
}

case class S_ADD(event: Student)
case class S_DELETE(event: Student)
case class S_UPDATE(event: Student)

case class T_ADD(event: Teacher)
case class T_DELETE(event: Teacher)
case class T_UPDATE(event: Teacher)


class SchoolMgtActor extends Actor {
  
  val studentActorRef = this.context.actorOf(Props[StudentActor], "StudentActor")
  context.watch(studentActorRef)
  
  val teacherActorRef = this.context.actorOf(Props[TeacherActor], "TeacherActor")
  context.watch(teacherActorRef);
  
  def receive = {
    case msg: S_ADD =>     studentActorRef.tell(msg, self)
    case msg: S_DELETE =>  studentActorRef.tell(msg, self)
    case msg: S_UPDATE =>  studentActorRef.tell(msg, self)
    case msg: T_ADD =>     teacherActorRef.tell(msg, self)
    case msg: T_DELETE =>  teacherActorRef.tell(msg, self)
    case msg: T_UPDATE =>  teacherActorRef.tell(msg, self)
    case msg: Any => {
      studentActorRef.tell(msg, self)
      teacherActorRef.tell(msg, self)
    }
  }
  
  /**
   * 采用OneForOneStrategy，仅对发生错误的Actor进行错误恢复
   */
  override val supervisorStrategy = OneForOneStrategy(){
      case _: RuntimeException =>
        Restart // 重启并替换原actor，重启后需恢复actor的状态（在post-restart中进行状态恢复）
  }
  
}

object SchoolMgtSystem extends App {
  val system = ActorSystem("lifeCycleSystem")
  val schoolMgtActorRef = system.actorOf(Props[SchoolMgtActor], "SchoolMgtActor")
  
  schoolMgtActorRef ! S_ADD(new Student(101,"s_101",91.0))
  schoolMgtActorRef ! S_ADD(new Student(102,"s_102",92.0))
  schoolMgtActorRef ! S_ADD(new Student(103,"s_103",93.0))
  schoolMgtActorRef ! S_DELETE(new Student(102,"s_102",92.0))
  schoolMgtActorRef ! S_UPDATE(new Student(101,"s_101",60.0))
  schoolMgtActorRef ! S_UPDATE(new Student(103,"s_103",100.0))
  
  schoolMgtActorRef ! T_ADD(new Teacher(201,"t_201"))
  schoolMgtActorRef ! T_ADD(new Teacher(202,"t_202"))
  schoolMgtActorRef ! T_ADD(new Teacher(203,"t_203"))
  schoolMgtActorRef ! T_DELETE(new Teacher(201,"t_201"))
  schoolMgtActorRef ! T_DELETE(new Teacher(202,"t_202"))
  schoolMgtActorRef ! T_UPDATE(new Teacher(203,"t_203_1"))
  
  
  schoolMgtActorRef ! "restart"
  
  TimeUnit.SECONDS.sleep(3)
  
  system.stop(schoolMgtActorRef)
  
  System.exit(0)
}