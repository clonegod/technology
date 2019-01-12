package scala09.homework

import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import akka.event._
import java.util.concurrent.TimeUnit
import java.io.PrintWriter
import java.io.File
import scala.collection.mutable.ListBuffer

object AkkaTest extends App {
  val system = ActorSystem("schoolSystem")
  val schoolOffice = system.actorOf(SchoolOffice.props, "office")
  println("school office created")
  
  schoolOffice ! SchoolOffice.ADD("S-xiaoming")
  schoolOffice ! SchoolOffice.ADD("S-xiaohong")
  schoolOffice ! SchoolOffice.ADD("T-mike")
  
  schoolOffice ! SchoolOffice.DELETE("S-xiaoming")
  
  // schoolOffice ! SchoolOffice.UPDATE("T-mike")
  
  TimeUnit.SECONDS.sleep(1)
  system.shutdown()
}

object SchoolOffice {
  def props = Props(new SchoolOffice)
  
  case class ADD(event: String)
  case class DELETE(event: String)
  case class UPDATE(event: String)
  
  def getEventKey(info: String) = {
     info.split("-")(0)
  }
  
  def getEventValue(info: String) = {
	  info.split("-")(1)
  }

  def persist(data: String, operType: String) = {
    println(operType+"\t"+data)
    val writer = new PrintWriter(new File("school_"+operType+"_"+System.currentTimeMillis()+".txt"))
    writer.write(data)
    writer.flush()
    writer.close()
  }
}

class SchoolOffice extends Actor {
  val log = Logging(context.system, this)
  import SchoolOffice._
  
  var persons = Map[String, List[String]]()
  
  def receive = {
    case add: SchoolOffice.ADD => {
      doAdd(add.event)
      persist(persons.mkString("\n"), "ADD")
    }
    case delete: SchoolOffice.DELETE => {
       doDelete(delete.event)
       persist(persons.mkString("\n"), "DELETE")
    }
    case update: SchoolOffice.UPDATE => {
      doUpdate(update.event) 
      persist(persons.mkString("\n"), "UPDATE")
    }
  }
  
  def doAdd(info: String) = {
    val list = persons.get(getEventKey(info))
    if(list != None) {
      persons += getEventKey(info) -> (getEventValue(info) :: list.get)
    } else {
      persons += getEventKey(info) -> (getEventValue(info) :: List())
    }
  }
  
  def doDelete(info: String) = {
    val list = persons.get(getEventKey(info))
    if(list != None) {
      val listbuffer = ListBuffer[String]()
      list.get.foreach { x => 
        if(! x.equals(getEventValue(info))) listbuffer.append(x)
      }
      persons += (getEventKey(info) -> listbuffer.toList)
    }
  }
  
  def doUpdate(info: String) {
    doDelete(info)
    doAdd(info)
  }
  
}