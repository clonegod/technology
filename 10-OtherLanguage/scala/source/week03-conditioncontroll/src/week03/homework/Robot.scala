package week03.homework

class Robot_Bob {
  
  def autoAnswer(hey: String): String = {
    val answer = 
      hey.trim() match {
        case "WATCH OUT!" => "Whoa, chill out!"  
        case "Do you speak English?" => "Sure."  
        case "DO YOU HEAR ME?" => "Whoa, chill out!"  
        case "What DID you do?" => "Sure."  
        case "" => "Fine. Be that way."
        case _ => "Whatever."
      }
    answer
  }
  
}

object Robot_Bob {
  def main(args: Array[String]) = {
    
    val bob = new Robot_Bob();
    
    val askList = List[String](
       "WATCH OUT!",
       "Do you speak English?",
       "DO YOU HEAR ME?",
       "What DID you do?",
       "   ",
       "Bye!"
    )
    
    for(ask <- askList) {
      print("[ask]: "); println(ask)
      print("[bob]: "); println(bob.autoAnswer(ask))
      println()
    }
    
  }
  
}