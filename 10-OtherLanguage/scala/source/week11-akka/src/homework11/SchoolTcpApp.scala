package homework11

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net._

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
}

class SimpleHandler extends Actor with ActorLogging {
  
  import Tcp.PeerClosed
  import Tcp.Received
  import Tcp.Write
  
  val schoolMgtActorRef = context.actorOf(Props[SchoolMgtActor], "SchoolMgtActor")
  
  override def receive = {
    case Received(data) => {
      /** 接收数据请求，对消息分类，发送到schoolMgtActorRef，进行学生或老师的增删改查操作 */
    	val content = data.decodeString("US-ASCII")
    	
			if(content.startsWith("S_ADD")) {
			  schoolMgtActorRef ! S_ADD(Student.fromStr(content.split(":")(1)))
			}
    	if(content.startsWith("S_DELETE")) {
    		 schoolMgtActorRef ! S_DELETE(Student.fromStr(content.split(":")(1)))
    	}
    	if(content.startsWith("S_UPDATE")) {
    		 schoolMgtActorRef ! S_UPDATE(Student.fromStr(content.split(":")(1)))
    	} 
    	if(content.startsWith("S_QUERY")) {
    		 schoolMgtActorRef ! S_QUERY()
    	} 
    	
    	if(content.startsWith("T_ADD")) {
    		schoolMgtActorRef ! T_ADD(Teacher.fromStr(content.split(":")(1)))
    	}
    	if(content.startsWith("T_DELETE")) {
    		schoolMgtActorRef ! T_DELETE(Teacher.fromStr(content.split(":")(1)))
    	}
    	if(content.startsWith("T_UPDATE")) {
    		schoolMgtActorRef ! T_UPDATE(Teacher.fromStr(content.split(":")(1)))
    	}
    	if(content.startsWith("T_QUERY")) {
    		schoolMgtActorRef ! T_QUERY()
    	}
    	
    	sender() ! Write(data)
//    	println("data received: " + content)
//    	log.info("data: {}", content)
//    	sender() ! Write(data)
      
    }
      
    case PeerClosed => context stop self
  }
}
