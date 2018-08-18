package part1_basic

/**
Groovy自动导入以下包和类(可以直接使用在这些包中的类，而不需要指定包的名称)：
	groovy.lang.* 
	groovy.util.* 
	java.lang.* 
	java.util.* 
	java.net.* 
	java.io.* 
	java.math.BigInteger 
	java.math.BigDecimal
*/

// java.net.URLEncoder.encode("a b");

// Groovy中方法调用的圆括号是可选的，这是建立在消除模棱两可的情况和groovy语言规范中概述优先处理规则上的.
def arg1 = URLEncoder.encode('a b', 'utf-8')
println arg1

def arg2 = URLEncoder.encode 'a b', 'utf-8'
println arg2
