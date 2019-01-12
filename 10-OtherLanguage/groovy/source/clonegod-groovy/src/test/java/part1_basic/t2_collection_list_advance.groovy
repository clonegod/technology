package part1_basic

// list query, iteration, and accumulation
/**
 * 查询方法包括查询list中元素数量的count方法，
 * min和max方法用来查询list中的最小元素和最大元素，
 * find方法用来查找list第一个符合闭包要求的元素，
 * every和any方法用来确定list中的每一个元素（或者任何一个元素）是否符合闭包的要求。
 *
 */
def list = [1,2,3,1]

// Querying
assert list.count(1) == 2
assert list.max() == 3
assert list.min() == 1

def even = list.findAll { item ->
	item % 2 == 0
}
assert even == [2]

assert list.every { item -> item < 5}
assert list.any { item -> item < 2}


// Iteration
/**
 * 使用each方法进行正向遍历，并且使用reverseEach方法进行反向遍历
 */
def store = ''
list.each { item -> 
	store += item
}
assert store == '1231'

store = ''
list.reverseEach { item ->
	store += item
}
assert store == '1321'


// Accumulation
/**
 * inject方法来自Smalltalk，这个方法使用闭包注入一个新的函数，
 * inject这个函数用来对一个中间结果和遍历的当前元素进行操作，inject方法的第一个参数是中间结果的初始值
 */
// join将所有元素通过给定的字符串进行连接，然后将结果作为字符串返回
assert list.join('-') == '1-2-3-1'

assert 0+1+2+3+4 == [1,2,3,4].inject(0) { acc, val -> acc + val }
assert 1 * 1*2*3*4 == [1,2,3,4].inject(1) { acc, val -> acc * val }
assert 2 * 1*2*3*1 == list.inject(2) { fac, item -> fac *= item }

assert 'The quick brown fox' ==
['quick', 'brown', 'fox'].inject('The') { acc, val -> acc + ' ' + val }

// min
assert 'bat' ==
['rat', 'bat', 'cat'].inject('zzz') { min, next -> next < min ? next : min }

// max
def max = { a, b -> [a, b].max() }
def animals = ['bat', 'rat', 'cat']
assert 'rat' == animals.inject('aaa', max)

