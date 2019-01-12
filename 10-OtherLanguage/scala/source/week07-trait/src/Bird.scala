

abstract class Bird {
  def name: String
}

trait Flyable {
  def fly() = println("I can fly")
}

trait Swimmable {
  def swim() = println("I can swim")
}

// 鸽子
class Pigeon extends Bird with Flyable {
  override def name = "I am a pigeon"
  
  override def fly() = println("I can fly fast")
}

// 企鹅
class Penguin extends Bird with Swimmable {
  override def name = "I am a penguin"
}

// 鹰
class Eagle extends Bird with Flyable {
  override def name = "I am an eagle"
}

// 鸵鸟
class Ostrich extends Bird {
  override def name = "I am an ostrich"
}

object Bird extends App {
  val birds = List(new Pigeon, new Pigeon, new Eagle)
  birds.foreach { x => x.fly() }
}