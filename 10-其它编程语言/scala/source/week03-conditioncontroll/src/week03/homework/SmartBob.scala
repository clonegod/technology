package week03.homework

class IntelligenceBob {
  
  class MappingItem (c: String, t: String) {
  	val category = c
  	val text = t
  }
  
  val mtList = List(
    new MappingItem("QUESTION", "Sure."),
    new MappingItem("SHOUT",     "Whoa, chill out!"),
    new MappingItem("BLANK",    "Fine. Be that way."),
    new MappingItem("DEFAULT",   "Whatever.")
  )
  
  def getCategory(text: String): String = {
    if(text == null) return "DEFAULT"
    
    val trimed = text.replaceAll("\\s+", "")
    
    // 全是空格或者tab
    if(trimed.length() == 0) return "BLANK"
    
    // 叫喊
		val content = trimed.substring(0, trimed.length()-1)
		if(content matches "^[A-Z]+$") return "SHOUT"
		
		// 问句
    val symbol = trimed.charAt(trimed.length()-1)
    if(symbol == '?') return "QUESTION"
    
    // 其它内容
    "DEFAULT"
  }
  
  def talk(hey: String): String = {
    for(item <- mtList if item.category == getCategory(hey)) return item.text
    "NEVER HAPPEN"
  }
  
}

object IntelligenceBob {
  
  def main(args: Array[String]) = {
    val bob = new IntelligenceBob();
    val askList = List[String](
       "WATCH OUT!",
       "Do you speak English?",
       "DO YOU HEAR ME?",
       "What DID you do?",
       "   ",
       null,
       "How are you!",
       "Bye!"
    )
    for(ask <- askList) {
      print("[ask]: "); println(ask)
      print("[bob]: "); println(bob.talk(ask))
      println()
    }
  }
  
}