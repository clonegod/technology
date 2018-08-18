package week04.homework

object ObjectFunctionSet {
  
  /**
   * 我们定义一个没有重复元素的列表为集合，但是为了表述类似所有整数这类的集合，实际上我们无法穷举所有的整数
   * 所以我们通过一个函数，来判断这个元素是否整数：例如给3，是否是负数，答案是否
   * 所以我们定义（x: Int) => Boolean这样的一个函数，作为判断一个整数是否是整数集合的判据
   * 通过定义一个alias来作为一个Set的定义：
   */
  type Set = Int => Boolean
  
  
  /**
   * 判断一个整数是否属于这个Set
   */
  def contains(s: Set, elem: Int) : Boolean = s(elem)
  
  
  /**
   * 实现一个创建只有一个整数的Set的函数，即返回只有一个指定元素的集合
   * 通过这样的函数，可以创建更大的Set
   */
  def singletonSet(elem: Int): Set = n => n == elem
  
  
  /**
   * 创建Set的union函数
   * 返回两个集合的并，集合的所有元素在集合s或 t中
   */
  def union(s: Set, t: Set): Set = i => (s(i) || t(i))
  
  
  /**
   * 创建Set的intersect函数
   * 返回两个集合的交，集合的所有元素在集合s和 t中
   */
  def intersect(s: Set, t: Set): Set = i => (s(i) && t(i))
  
  
  /**
   * 创建Set的diff函数
   * 返回两个集合的差, 集合的所有元素在集合s中，但不在集合 t中
   */
  def diff(s: Set, t: Set): Set = i => (s(i) && !t(i))
  
  
  /**
   * 创建Set的filter函数，通过filter创建一个新的Set
   */
  def filter(s: Set, p: Int => Boolean): Set = i => (s(i) && p(i))
  
  // 注：为便于测试验证，指定一个边界值
  val bound = 10000;
  
  // 打印Set中的元素
  def toString(s: Set): String = {
    val coll = for ( i <- -bound to bound if contains(s, i)) yield i
    coll.mkString( "{", "," , "}" )
  }
  
  /**
   * 判断是否所有Set s的成员都满足条件p
   * => s为判断条件p“所指代的某个集合”的子集
   * 思路：
   * 从下边界开始到上边界结束，如果区间内有任何一个元素被否定，则判定为不满足，返回false；
   * 如果一直比较到上边界了，则认为Set s的每个成员都满足条件p，返回true
   */
  def forall(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a > bound) true // 若已超出上界，返回true
      else if (s(a) && !p(a)) false // p(a)为假，就返回false
      else iter(a+1) // 取下一个进行判断
    }
    iter(-bound) // 从最小开始
  }
  
  /**
   * 判断Set s是否至少有一个成员满足条件p
   * 思路：
   * 从下边界开始到上边界结束，如果区间内有任何一个元素满足条件，则判定不满足，返回true；
   * 如果一直比较到上边界了，则认为Set s中没有一个成员满足条件p，返回false
   */
  def exists(s: Set, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (a>bound) false
      else if ((s(a)&&p(a))) true
      else iter(a+1)
    }
    iter(-bound)
    
  }
  
  /**
forall(x,y)其实就是判断前者x是否是后者y的子集
而exists就是算出两个集合有没有非空公共子集,但是有个问题:空集是任何集合的子集,不能直接用interfact
===>
exists 等价于 两个集合x,y有非空公共子集,等价于两个集合x,y没有非空公共子集的取反,等价于x集合取反是y的子集 逻辑与 && y集合取反是x的子集
但是一个集合的取反要靠辅助函数构造一个全集,然后diff(全集,某个集合) 实现,于是最后代码如下:
!(forall(s, diff(ful, p)) && forall(p, diff( ful, s)))
   * */
  def exists2(s: Set, p: Int => Boolean): Boolean = {
		def iter(a: Int, set: Set): Set = {
      if (a > bound) set
      else iter(a + 1, union(set, singletonSet(a)))
    }
    val ful = iter(- bound, Set())
    !(forall(s, diff(ful, p)) && forall(p, diff( ful, s)))
		  
  }
  
  
  /**
   * 测试验证
   */
  def main(args: Array[String]) = {
    val s1 = singletonSet(1)
		val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    
    // s1和s2的交集为{1,2}
    val s4 = union(s1, s2)
    println(toString(s4))
    
    // s2和s3的交集为{2,3}
    val s5 = union(s2, s3)
    println(toString(s5))
    
    // s4和s5的交集为{2}
    val s6 = intersect(s4, s5)
    println(toString(s6))
    
    // 1在s4中，且不在s5中，所以差集为{1}
    val s7 = diff(s4, s5)
    println(toString(s7))
    
    // s3并不在s4集合中存在，所以返回空集{}
    val s8 = filter(s4, s3)
    println(toString(s8))
    
    // s4中的所有元素都在s4_plus中
    var s4_plus = union(s4,s5)
    val s9 = forall(s4, s4_plus)
    println(s9)
    
    // s3中存在1个元素属于s4_plus
    val s10 = exists(s3, s4_plus)
    println(s10)
    
    
  }
}