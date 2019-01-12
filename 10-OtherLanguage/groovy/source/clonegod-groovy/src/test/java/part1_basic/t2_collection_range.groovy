package part1_basic

// 范围（Ranges）- 一个有效的开始点和一个结束点 。
// 写法：   start..end

/**     Range 应用于整数范围        */
def x = 1..10 
assert x.contains(5) 
assert x.contains(15) == false 
assert x.size() == 10 
assert x.from == 1 
assert x.to == 10 
assert x.reverse() == 10..1

// Inclusive ranges
assert (1..10).contains(1)
assert (1..10).contains(5)
assert (1..10).contains(10)
assert (1..10).contains(0) == false
assert (1..10).contains(11) == false

// Half-exclusive ranges
assert (1..<10).contains(9)
assert (1..<10).contains(10) == false


// References to ranges
def a = 0..10
assert a instanceof Range
assert a.contains(6)

// Explicit construction
a = new IntRange(1, 10)
assert a.contains(5)


/**     Range 应用于非数值类型        */
// Date ranges
// GDK增加了previous和next方法到java.util.Date，这两个方法是对日期进行增加一天或者减少一天的方法。
// GDK也增加minus和plus方法给java.util.Date，这样可以对日期加数天或者减数天。
def today = new Date()
def yesterday = today - 1
assert (yesterday..today).size() == 2

// String ranges
assert ('a'..'c').contains('b')

// for in range loop
def log = ''
for (element in 5..9) {
	log += element
}
assert log == '56789'

// loop with reverse range
log = ''
for (element in 9..5) {
	log += element
}
assert log == '98765'


// Half-exclusive, reverse, each with closure
log = ''
(9..<5).each { element -> 
	log += element
}
assert log == '9876'


