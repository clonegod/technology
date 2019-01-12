package homework.router

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Terminated
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import scala.collection.mutable.ListBuffer
import akka.util.ByteString

case class Query(studentList:ListBuffer[Student], original_sender: ActorRef, client_sender: ActorRef)
case class Reply(msg: String, original_sender: ActorRef, client_sender: ActorRef)

/**
 * 执行具体查询任务的Actor
 */
class Worker extends Actor with ActorLogging {

  def receive = {
    case q: Query => 
      println("instruction received from master: " + q)
      // 将查询结果返回给Master（简单返回所有用户信息）
      sender().!(Reply(
          q.studentList.mkString("\n"), 
          q.original_sender, 
          q.client_sender)
      )(context.parent)
  }
}

class Master extends Actor with ActorLogging{
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Worker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case q: Query =>
      router.route(q, self)
    case Reply(msg, original_sender, client_sender) => 
      println("msg from worker: " + msg)
      // master将消息返回给server
      original_sender.tell(ByteString.fromString(msg), client_sender)
    case Terminated(a) => {
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Worker])
      context watch r
      router = router.addRoutee(r)
    }
  }
}

object Master {
  val props = Props(new Master)
}
