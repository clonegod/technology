package part1_basic

// 2. GString 在双引号的字符串中允许使用占位符，占位符在需要的时候将自动解析，这是一个GString类型

// GString增强型字符串。 GString是groovy.lang.GString的实例，
println "hello Dierk"

def lines = """first line
second line
third line
"""
println lines

// GString必须适使用双引号，允许有占位符并且允许在运行时对占位符进行解析和计算
def nick = 'Gina'
def book = 'Groovy in Action'
assert "$nick is $book" == 'Gina is Groovy in Action'



// GString - 简单的$语法
me = 'Tarzan'
you = 'jane'
line = "me $me - you $you"
assert line == 'me Tarzan - you jane'

// GString - 扩展的简单语法
date = new Date(0)
out = "Year $date.year Month $date.month Day $date.date"
assert out == 'Year 70 Month 0 Day 1'

// GString - 使用花括号表示参数引用的完整语法
out = "Date is ${date.toGMTString()} !"
assert out == "Date is 1 Jan 1970 00:00:00 GMT !"

// GString - 多行的GString
sql = """
SELECT FROM MyTable
	WHERE Year = $date.year
"""
assert sql == """
SELECT FROM MyTable
	WHERE Year = 70
"""

// GString - $作为字符串的一部分
out = "my 0.02\$"
assert out == 'my 0.02$'
