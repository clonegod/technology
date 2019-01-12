package homework.router

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.net.InetSocketAddress

import scala.collection.mutable.ListBuffer

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.io.IO
import akka.io.Tcp
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Router
import akka.util.ByteString
import akka.util.ByteStringBuilder

object TcpApp extends App {
  
  val system = ActorSystem("school-service-system")
  val endpoint = new InetSocketAddress("localhost", 11111)
  system.actorOf(SchoolMgtService.props(endpoint), "school-service")
  
  readLine(s"Hit ENTER to exit ...${System.getProperty("line.separator")}")
  system.shutdown()
}

object SchoolMgtService {
  def props(endpoint: InetSocketAddress): Props =
    Props(new SchoolMgtService(endpoint))
}

class SchoolMgtService(endpoint: InetSocketAddress) extends Actor with ActorLogging {
  import context.system
  import Tcp.Bound
  import Tcp.Connected
  import Tcp.Register
  
  IO(Tcp) ! Tcp.Bind(self, endpoint)
  
  override def receive = {
    case b @ Bound(address) =>
      log.info("bound on address: {}", address)
      
    case c @ Connected(remote, local) =>
      val handler = context.actorOf(SimpleHandler.props)
      val connection = sender()
      connection ! Register(handler, keepOpenOnPeerClosed = true)
      log.info("remote {} connected at {}", remote, local)
  }
}

object SimpleHandler {
  def props : Props = Props(new SimpleHandler)
  
  val NEW_LINE = System.getProperty("line.separator")
  val dataPath = "/student.txt";
  var studentList = ListBuffer[Student]()
  
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
    	writer.write(NEW_LINE)
    }
    writer.flush()
    writer.close()
  }
}

class SimpleHandler extends Actor with ActorLogging {
  
  import SimpleHandler._
  import Tcp.PeerClosed
  import Tcp.Received
  import Tcp.Write
  
  override def receive = {
    case Received(data) => { // tcp client send ByteString
      /** 接收数据请求，对消息分类，调用对应的CRUD方法 */
    	val content = data.decodeString("US-ASCII")
    	log.info("msg received:: {}", content)
    	
			if(content.startsWith("S_ADD")) {
			  add(Student.fromStr(content.split(":")(1)))
			  sender() ! Write(ByteString.fromString("S_ADD RECEIVED"))
			}
    	if(content.startsWith("S_DELETE")) {
    	  delete(Student.fromStr(content.split(":")(1)))
    	  sender() ! Write(ByteString.fromString("S_DELETE RECEIVED"))
    	}
    	if(content.startsWith("S_UPDATE")) {
    	  delete(Student.fromStr(content.split(":")(1)))
    	  add(Student.fromStr(content.split(":")(1)))
    	  sender() ! Write(ByteString.fromString("S_UPDATE RECEIVED"))
    	} 
    	if(content.startsWith("S_QUERY")) {
    	  val system = ActorSystem("routerSystem");
        val master = system.actorOf(Master.props, "router");
        // 将tcpclient 绑定到该查询对象上，供返回结果时用。否则，返回的结果不知道返回给哪个客户端！
        master ! new Query(studentList, self, sender)
    		//sender() ! Write(ByteString.fromString(NEW_LINE+studentList.mkString(NEW_LINE)))
    	} 
    }
      
    case PeerClosed => context stop self
    
    /**
     * 将master查询结果返回给tcp客户端
     * 此处的sender()：在master中传递的原始tcp_client的ActorRef
     */
    case msg:ByteString => {
      println("msg from master: " + msg.decodeString("US-ASCII"))
      sender() ! Write(ByteString.fromString(
          NEW_LINE +
          "msg from master: " + 
          NEW_LINE +
          msg.decodeString("US-ASCII")))
    }
  }
  
}
