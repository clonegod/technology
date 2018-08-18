package week02.homework

/**
 * 利用堆栈数据结构来实现括号匹配算法
 */
class BracketMatcher {
  def balance(chars: List[Char]): Boolean = {
    // 过滤掉干扰字符
    val filtedChars = filter(chars)
    
		// 下面开始对剩下的字符进行检查
    
    // 创建一个空的List
    var stack = List[Char]()
    
    // 括号匹配原则：
    // 从左边开始先出现若干左括号，接着再出现右括号或者左括号，以此类推。
    // 每一个出现的右括号都将与左边某个左括号进行匹配
    for(char <- filtedChars) {
      if(char == '(') {
        // 左括号入栈
        stack = stack :+ char
      } else if(char == ')'){
    	  // 栈空（表示没有左括号），或者栈顶元素为右括号，都认为失败
        if(stack.isEmpty || stack.take(1)==char) return false
        
        // 删除栈顶元素-左括号
        stack = stack.drop(1)
      } else {
        // cann't happen
      }
    }
    
    // 经过上面的匹配后，如果List为空，则表示括号配对成功，否则视为失败
    stack.isEmpty
  }
  
  /**
   * 过滤掉非'('或')'的字符
   */
  def filter(chars: List[Char]): List[Char] = {
    chars.filter((c: Char) => (c == '(' || c == ')'))
  }
  
}

object BracketMatcher extends App {
	
  val text1 = "(if (zero? x) max (/ 1 x))";
  val text2 = "I told him (that it’s not (yet) done). (But he wasn’t listening)";
  val text3 = "--)";
  val text4 = "(()))";
  
  val matcher = new BracketMatcher();
  var isMatch = false;
  
  isMatch = matcher.balance(text1.toList)
  println(text1+": " + isMatch)
  
  isMatch = matcher.balance(text2.toList)
  println(text2+": " + isMatch)
  
  isMatch = matcher balance text3.toList
  println(text3+": " + isMatch)
  
  isMatch = matcher balance text4.toList
  println(text4+": " + isMatch)
  
}