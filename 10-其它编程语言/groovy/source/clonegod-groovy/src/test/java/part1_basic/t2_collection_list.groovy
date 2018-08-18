package part1_basic

myList = [1,2,3]

assert myList.size() == 3
assert myList[0] == 1
assert myList instanceof ArrayList

emptyList = []
assert emptyList.size() == 0

longList = (0..100000).toList()
assert longList[555] == 555

explicitList = new ArrayList()
explicitList.addAll(myList)
assert explicitList.size() == 3
explicitList[0] = 10 // 赋值
assert explicitList[0] == 10 
assert explicitList[1] == 2
assert explicitList[2] == 3

explicitList = new LinkedList(myList)
assert explicitList.size() == 3
explicitList[0] = 10 // 赋值
assert explicitList[0] == 10 
assert explicitList[1] == 2
assert explicitList[2] == 3
assert explicitList == [10, 2, 3] // 列表的equals方法用来测试两个集合是否有相同的元素

//=================================================
// 空的list
def emptyList = []

// list的缺省类型是java.util.ArrayList
assert [] instanceof ArrayList

// 使用方括号构建List
def roman = ['', 'I', 'II', 'III', 'IV', 'V', 'VI', 'VII']

// 通过下标访问list的元素
assert roman[4] == 'IV'

// 往list中添加元素---自动扩张列表
roman[8] = 'VIII'
assert roman.size() == 9

// 通过range构建list
def list1 = ('a'..'e').toList()
assert list1 instanceof ArrayList


// 遍历list
def classes = [String, List, File]
for (clazz in classes) {
	print clazz.'package'.name + " "
}
// 简化for循环
println ([String, List, File].'package'.name)



//=================================================
// 使用list操作符 - 范围作为下标使用来访问list中的元素
myList = ['a', 'b', 'c', 'd', 'e', 'f']

assert myList[0..2] == ['a', 'b', 'c'] // getAt(range)
assert myList[0,2,4] == ['a', 'c', 'e'] // getAt(collection of indexes) 

myList[0..2] = ['x','y','z'] // putAt(range)
assert myList == ['x', 'y', 'z', 'd', 'e', 'f']

myList[3..5] = [] // Removing elements ，当指定的列表值小于给定的范围或者是空的时候，这个列表被收缩
assert myList == ['x','y','z']

myList[1..1] = ['y', '1', '2'] // Adding elements 当指定的列表值更大时，列表进行增长
assert myList == ['x', 'y', '1', '2', 'z']

// 除了正数作为下标索引之外，list也可以通过负数进行索引
assert myList[-1] == 'z'
assert myList[1..-2] == ['y', '1', '2'] // 去掉原来列表的开始的和最后的元素

//=================================================
// 增加和删除列表中的条目
// plus(Object),plus(Collection),leftShift(Object),minus(Collection) 和multiply
myList = []

myList += 'a' // plus(Object)
assert myList == ['a']

myList += ['b', 'c'] // plus(collection)
assert myList == ['a', 'b', 'c']

myList = []
myList << 'a' << 'b' // leftShif like append
assert myList == ['a', 'b']

assert myList - ['b'] == ['a'] // minus collection

assert myList * 2 == ['a', 'b', 'a', 'b'] // multiply


//=================================================
// Lists taking part in control structures
myList = ['a', 'b', 'c']

assert myList.isCase('a')
candidate = 'a'
switch(candidate) { // Classify by containment
	case myList: assert true; break
	default: assert false
}

assert ['x', 'a', 'z'].grep(myList) == ['a'] // Intersection filter

myList = []
if(myList) assert false // empty list is false

log = ''
for (i in [1, 'x', 5]) {
	log += i
}
assert log == '1x5'


//=================================================
// 使用列表方法
assert [1,[2,3]].flatten() == [1,2,3]

assert [1,2,3].intersect([4,3,1]) == [3,1] // 交集
assert [1,2,3].disjoint([4,5,6]) // 无交集

// take list like a stack
list = []
list[0] = 1
list.push(2)
list << 3
popped = list.pop()
assert popped == 3
assert list == [1,2]

assert [1,2].reverse() == [2,1]

assert [3,1,2].sort() == [1,2,3]

// compare list by first element
def list = [[1,0], [0,1,2]]
list = list.sort({a, b -> a[0] <=> b[0]})
assert list == [[0,1,2], [1,0]]

// compare list by size
list = list.sort { item -> item.size() }
assert list == [[1,0], [0,1,2]]


// removing by index
list = ['a', 'b', 'c']
list.remove(2)
assert list == ['a', 'b']

// removing by value
list.remove('b')
assert list == ['a']

// transforming one list into another
def doubled = [1,2,3].collect { item -> item * 2}
assert doubled == [2, 4, 6]

// finding every element matching the closure
def odd = [1,2,3].findAll { item -> item % 2 == 1 }
assert odd == [1,3]

//=================================================
// 列表的两个相关的问题是删除重复值和删除null值

// 删除重复值的一种方法是将list转换为一个Set，使用list作为参数通过调用Set的构造方法来完成
def x = [1,1,1] 
assert [1] == new HashSet(x).toList() 
assert [1] == x.unique() // 更简单的写法

// 从list中删除null值能保证列表中没有空元素
x = [1,null,1] 
assert [1,1] == x.findAll{it != null} 
assert [1,1] == x.grep{it} // 更简单的写法

