package homework11

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
    case msg:S_QUERY =>
      println("StudentActor S_QUERY received:"+msg.toString)
      sender() ! studentList.mkString("\n") // 客户端接收不到这里发送的消息，什么原因？
    case msg:Any => 
      println("ERROR:=>"+msg)
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
      studentList.append(Student.fromStr(line))
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
    case msg: T_QUERY =>
      sender() ! teacherList.mkString("\n")
    case msg:Any => 
      println("ERROR:=>"+msg)
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
      teacherList.append()
    file.close
  }
}

case class S_ADD(event: Student)
case class S_DELETE(event: Student)
case class S_UPDATE(event: Student)
case class S_QUERY()

case class T_ADD(event: Teacher)
case class T_DELETE(event: Teacher)
case class T_UPDATE(event: Teacher)
case class T_QUERY()


class SchoolMgtActor extends Actor {
  
  val studentActorRef = this.context.actorOf(Props[StudentActor], "StudentActor")
  context.watch(studentActorRef)
  
  val teacherActorRef = this.context.actorOf(Props[TeacherActor], "TeacherActor")
  context.watch(teacherActorRef);
  
  def receive = {
    case msg: S_ADD =>     studentActorRef.tell(msg, sender())
    case msg: S_DELETE =>  studentActorRef.tell(msg, sender())
    case msg: S_UPDATE =>  studentActorRef.tell(msg, sender())
    case msg: S_QUERY =>   studentActorRef.tell(msg, sender())
    case msg: T_ADD =>     teacherActorRef.tell(msg, sender())
    case msg: T_DELETE =>  teacherActorRef.tell(msg, sender())
    case msg: T_UPDATE =>  teacherActorRef.tell(msg, sender())
    case msg: T_QUERY =>  teacherActorRef.tell(msg, sender())
    case msg:Any => 
      println(msg)
  }
  
  /**
   * 采用OneForOneStrategy，仅对发生错误的Actor进行错误恢复
   */
  override val supervisorStrategy = OneForOneStrategy(){
      case _: RuntimeException =>
        Restart // 重启并替换原actor，重启后需恢复actor的状态（在post-restart中进行状态恢复）
  }
  
}