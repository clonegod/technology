package part1_basic

// 在数字上调用方法
def x = 1
def y = 2
assert x + y == 3
assert x.plus(y) == 3
assert x instanceof Integer


// 在对象上进行运算
def i = 'a'
assert i * 2 == 'aa'


// Math api
assert Math.abs(-1) == 1
assert Math.round(1.234) == 1

assert '65'.toInteger() == 65

// 数字后面出现G,如果数字是整数，则为BigInteger类型
def g = 100G
println g instanceof BigInteger

// BigDecimal是默认的非整数类型——除非指定后缀强制类型为Float或者Double，否则将使用BigDecimal。
// 数字后面出现G,如果数字是小数，则为BigDecimal类型
def bd = 1.23g
println bd instanceof BigDecimal

// 对于除法运算，整数除法：
assert 1 / 2 == 0.5

// 对于除法运算，小数除法：
// 如果任何一个数是Float（或者Double）类型，那么运算结果为Double类型.
// 否则，结果为BigDecimal类型，精度与两个数的精度值较大的为准，采用四舍五入的方式
assert 9.000f / 3 instanceof Double
assert 9.000d / 3 instanceof Double
assert 9.000g / 3 instanceof BigDecimal

assert 1.000f / 3 == 0.3333333333333333
assert 1.000d / 3 == 0.3333333333333333
assert 1.000g / 3 == 0.3333333333

// ---------------------------------------------------

// GDK也定义了times、upto、downto和step方法，这些方法接受一个闭包参数

// Replication
def store = ''
5.times {
	store += 'x'
}
assert store == 'xxxxx'

// Walking up with loop variable
store = ''
1.upto(5) { number ->
	store += number
}
assert store == '12345'

// Walking down
store = ''
2.downto(-2) { number ->
	store += number + ' '
}
assert store == '2 1 0 -1 -2 '

// Walking with step width
store = ''
0.0.step(0.5, 0.1) { number ->
	store += number + ' '
}
assert store == '0.0 0.1 0.2 0.3 0.4 '

