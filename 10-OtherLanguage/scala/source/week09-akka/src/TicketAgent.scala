import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event._
import com.sun.media.sound.SoftMixingMixer.Info
import akka.actor.ActorRef
import java.util.concurrent.TimeUnit

object MainApp extends App {
  val system = ActorSystem("ticketingSystem")
  val boxOffice = system.actorOf(BoxOffice.props, "office")
  println("box office created")
  
  // 初始化
  boxOffice ! BoxOffice.Supply("Butterfly", 10)
  boxOffice ! BoxOffice.Request("Butterfly", 3)
  boxOffice ! BoxOffice.Request("Butterfly", 12)
  
  TimeUnit.SECONDS.sleep(3)
  system.shutdown()
}


object TicketSeller {
  def props(event: String) = Props(new TicketSeller(event))
  case class Add(numberOfTickets: Int)
  case class Buy(tickets: Int)
  case class Tickets(count: Int)
}

class TicketSeller(val event: String) extends Actor {
  import TicketSeller._
  
  var tickets: Int = 0
  val log = Logging(context.system, this)
  
  def receive = {
    case Add(newTickets) => {
      tickets = tickets + newTickets
      log.info("add tickets for " + event + ", now " + tickets + " tickets")
    }
    case Buy(nrOfTickets) => {
      if(tickets < nrOfTickets) {
        sender ! BoxOffice.SoldOut(this.event)
        log.info("ticket for " + event + " sold out")
      } else {
        tickets = tickets - nrOfTickets
        sender ! Tickets(nrOfTickets)
        log.info("tickets for " + event + "now at: " + tickets)
      }
    }
  }
  
}

object BoxOffice {
  def props = Props(new BoxOffice)
  
  case class Supply(event: String, tickets: Int)
  case class Request(event: String, tickets: Int)
  case class SoldOut(event: String)
}

class BoxOffice extends Actor {
  val log = Logging(context.system, this)
  var sellers = Map[String, ActorRef]()
  
  def createTicketSeller(event: String): ActorRef = {
    if(sellers.contains(event)) {
      sellers(event)
    } else {
      val seller = context.actorOf(TicketSeller.props(event), event)
      sellers += event -> seller
      seller
    }
  }
  
  def receive = {
    case supply: BoxOffice.Supply => {
      val seller = createTicketSeller(supply.event)
      seller ! TicketSeller.Add(supply.tickets)
    }
    case req: BoxOffice.Request => {
      val seller = createTicketSeller(req.event)
      seller ! TicketSeller.Buy(req.tickets)
    }
    case soldout: BoxOffice.SoldOut => {
      println("tickets for " + soldout.event + " sold out")
    }
  }
  

}