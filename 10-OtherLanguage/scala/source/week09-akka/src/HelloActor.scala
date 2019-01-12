import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import java.util.concurrent.TimeUnit

class HelloActor extends Actor{
  def receive = {
    case "hello"  => println("hello back to you.")
    case _        => println("huh?")
  }
}

object Test1_HelloActor extends App {
  
  var m = Map[String, List[String]]()
  val list1 = "x" :: "y" :: Nil
  
  println(m.get("a").get.dropWhile { x => x.equals("xy") })
  
  // actor need an ActorSystem
  val system = ActorSystem("HelloSystem")
  // create and start the actor
  val helloActor = system.actorOf(Props[HelloActor], name="helloActor")
  // send two messages
  helloActor ! "hello"
  helloActor ! "what"
  
  // shutdown the actor system
  TimeUnit.SECONDS.sleep(3)
   system.shutdown
}