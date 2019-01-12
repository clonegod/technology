package part1_basic

import groovy.json.StringEscapeUtils

assert "abc" == /abc/
assert "\\d" == /\d/

def reference = "hello"
assert reference == /$reference/

assert "\$" == /$/


/**
 * 用/…/语法声明了一个模式
 * 使用“=~”来根据模式匹配给定字符串
 */

 assert "123456" =~ /\d+/
 
 assert 'xxxxxx' == '123456'.replaceAll(/\d/, 'x')
 
 // regex查找操作符  =~  子串的匹配模式		（对字符串应用模式，返回一个Matcher）
 // regex匹配操作符  ==~	验证整串是否完全与模式匹配  （检测整个字符串是否与模式匹配，返回boolean）
 // regex模式操作符  ~String  （预先编译模式，返回Pattern）
 
def matcher = "1.1 我是一个标题" =~ /^\d+(?:\.\d+)? (.*)/
if(matcher.find()) {
	println matcher.group(1)
}

assert "2018/03/28" ==~ /\d{4}\/\d{2}\/\d{2}/


// ===================================================================
/**
把正则表达式应用到一个字符串上，groovy支持如下的任务：
 模式是否完全匹配整个字符串。
 在字符串中检查是否匹配上一个模式。
 匹配成功的数目。
 根据每个成功匹配的结果做一些工作。
 替换成功匹配的结果。
 使用匹配的结果作为分隔符将字符串的分割成多个字符串。
*/

twister = 'she sells sea shells at the sea shore of seychelles'
assert twister =~ /s.a/

// twister 必须包含这样一个子串：s开头，a结尾，且子串长度为3
finder = (twister =~ /s.a/)
assert finder instanceof java.util.regex.Matcher

// twister整个字符串中的单词只被单个空格分割
assert twister ==~ /(\w+ \w+)*/

WORD = /\w+/
matches = (twister ==~/($WORD $WORD)*/)
assert matches instanceof java.lang.Boolean

assert (twister ==~ /s.e/) == false // Match is full, not partial like find. ==~是更严格的匹配，是对整个字符串进行匹配。

wordsByX = twister.replaceAll(WORD, 'x')
assert wordsByX == 'x x x x x x x x x x'

words = twister.split(/\s+/)
assert words.size() == 10
assert words[0] == 'she'


// ===================================================================
myFairString = 'The rain in Spain stays mainly in the plain!'

BOUNDS = /\b/
rhyme = /$BOUNDS\w*ain$BOUNDS/

// string.eachMatch
found = ''
myFairString.eachMatch(rhyme, {match -> 
	found += match + ' '
})
assert found.trim() == 'rain Spain plain'

// matcher.each
found = ''
(myFairString =~ rhyme).each({match -> 
	found += match + ' '
}) 
assert found.trim() == 'rain Spain plain'

// string.replaceAll
cloze = myFairString.replaceAll(rhyme) {
	println it // it 是内置变量，引用匹配的子字符串
	it - 'ain' + '___'
}
assert cloze == 'The r___ in Sp___ stays mainly in the pl___!'


assert "hello world".replaceAll("o") { it.toString().toUpperCase() } == "hellO wOrld"

// ===================================================================
// GDK 对 matcher的增强

// \S 非空格字符
matcher = 'a* b c' =~ /\S+/
assert matcher[0] == 'a*'
assert matcher[1..2] == ['b', 'c']
assert matcher.count == 3

matcher = 'a:1 b:2 c:3' =~ /(\S+):(\S+)/ 
assert matcher.hasGroup() 
assert matcher[0] == ['a:1', 'a', '1']  // mattcher 返回一个数组, 索引为0的元素表示匹配到的字符串，后面的其它元素表示分组信息


// matcher的each方法，通过闭包来接收匹配结果
('xy' =~ /(.)(.)/).each { all, x, y -> 
	assert all == 'xy' 
	assert x == 'x' 
	assert y == 'y' 
}

// ===================================================================
// Pattern - 预编译模式，速度更快
// ~String 模式操作符把模式创建的时间从模式匹配的时间中分离出来

twister = 'she sells sea shells at the sea shore of seychelles'
regex = /\b(\w)\w*\1\b/

start = System.currentTimeMillis();
100000.times {
	twister =~ regex  // twister进行模式匹配
}

first = System.currentTimeMillis() - start

start = System.currentTimeMillis()
pattern = ~regex // 预编译模式
100000.times {
	pattern.matcher(twister)
}
second = System.currentTimeMillis() - start

assert first > second * 1.2


// ===================================================================
// Pattern对象是通过模式操作符返回的对象

// isCase（String）方法，该方法用来比较一个字符串是否完全匹配一个模式
assert (~/..../).isCase('bear')

// 模式分类匹配
switch('bear') {
	case ~/..../: assert true; break
	default: assert false
}

// 基于模式过滤数组元素
beasts = ['bear', 'wolf', 'tiger', 'regex']
assert beasts.grep(~/..../) == ['bear', 'wolf']



