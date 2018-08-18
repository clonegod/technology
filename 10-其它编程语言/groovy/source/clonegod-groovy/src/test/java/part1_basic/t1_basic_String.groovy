package part1_basic

// 在groovy中，字符串能出现在单引号或者双引号中
// 1. 普通字符串。一般的字符串是java.lang.String的实例,单引号所表示的字符串不会按照GString的类型来处理内容
println 'hello Dierk'

def total = '''-------
Total: $100.00
------'''
println total

greeting = 'Hello Groovy!'

assert greeting.startsWith('Hello')

assert greeting.getAt(0) == 'H'
assert greeting[0] == 'H'

assert greeting.indexOf('Groovy') >= 0
assert greeting.contains('Groovy')

assert greeting[6..11] == 'Groovy'

assert 'Hi' + greeting - 'Hello' == 'Hi Groovy!'

assert greeting.count('o') == 3

assert 'x'.padLeft(3) == '  x'
assert 'x'.padLeft(3, '_') == '__x'
assert 'x'.center(3) == ' x '
assert 'x' * 3 == 'xxx'


// StringBuffer Usage
greeting = 'Hello'

// 追加文本和赋值一起完成（下面的表达式执行完成后，greeting才变为StringBuffer）
greeting <<= ' Groovy'
assert greeting instanceof java.lang.StringBuffer

// 在StringBuffer上追加文本（此时greeting已经是StringBuffer类型了）
greeting << '!'
assert greeting.toString() == 'Hello Groovy!'

// 子串替换：将子串'ello'替换为'i'
greeting[1..4] = 'i'
assert greeting.toString() == 'Hi Groovy!'


