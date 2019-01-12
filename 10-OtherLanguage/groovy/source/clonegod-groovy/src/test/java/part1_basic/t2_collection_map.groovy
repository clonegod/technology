package part1_basic

// 简单的映射（maps）
// 一般情况下key的类型都是字符串，在声明map的时候可以忽略字符串标记（单引号或者双引号）
assert ['a':1] == [a:1]

def x = 'a'
assert ['x':1] == [x:1]
assert ['a':1] == [(x):1] // 把符号放在圆括号中，强制让groovy将符号看做为一个表达式

// 一个用来表示http返回代码和他们的描述信息的map
def http = [
	100 : 'CONTINUE',
	200 : 'OK',
	400 : 'BAD REQUEST' ] 

assert http[200] == 'OK' 

http[500] = 'INTERNAL SERVER ERROR' 
assert http.size() == 4

// ===========================
// map缺省的类型是java.util.HashMap, 也可以显示构造其它类型的Map
myMap = [a:1, b:2, c:3]

assert myMap instanceof HashMap
assert myMap.size() == 3
assert myMap['a'] == 1

def emptyMap = [:]
assert emptyMap.size() == 0

def explicitMap = new TreeMap()
explicitMap.putAll(myMap)
assert explicitMap['c'] == 3

// ===========================
myMap = [:]
myMap.name = 'alice'
myMap.age = 20
myMap.'address.home' = 'sichuan'
myMap.'address.work' = 'beijing'
println myMap

// ===========================
// Query on map
myMap = [a:1, b:2, c:3]
def other = [b:2, c:3, a:1]

assert myMap == other

// normal jdk methods
assert myMap.isEmpty() == false
assert myMap.size() == 3

assert myMap.containsKey('a')
assert myMap.containsValue(1)

assert myMap.keySet() == toSet(['a','b','c'])
assert toSet(myMap.values()) == toSet([1,2,3])
assert myMap.entrySet() instanceof Collection

// Methods added by GDK
assert myMap.any { entry -> entry.value > 2} 
assert myMap.every { entry -> entry.key < 'd'}

// utility method used for assertion
def toSet(list) {
	return new java.util.HashSet(list);
}



//=======================================
// iterating over maps
myMap = [a:1, b:2, c:3]

myMap.keySet().each { println it}
myMap.values().each { println it}

// 传递一个参数给闭包，那么这个参数就是map的一个entry
myMap.each {entry -> 
	println entry.key + "=" + entry.value
}

// 传递两个参数给闭包，那么参数就是key和value
myMap.each {key, value -> 
	println key + "=" + value
}

//=======================================
// changing map content and building new objects from it 
myMap = [a:1, b:2, c:3]
myMap.clear()
assert myMap.isEmpty()

myMap = [a:1, b:2, c:3]
myMap.remove('a')
assert myMap.size() == 2

myMap = [a:1, b:2, c:3]
def abMap = myMap.subMap(['a','b']) // 通过一个key的集合来过滤原始的map来获取到一个新的map。
assert abMap.size() == 2

abMap = myMap.findAll { entry -> entry.value < 3}
assert abMap.size() == 2
assert abMap.a == 1

// collect 方法返回一个list列表
def doubled = myMap.collect { entry -> entry.value *= 2 }
assert doubled instanceof List
assert doubled.every { item -> item % 2 == 0}

// 可以事先创建好list集合，传入到闭包中填充元素
def addTo = []
myMap.collect(addTo) { entry -> entry.value *= 2}
assert addTo instanceof List
assert addTo.every { item -> item % 2 == 0}





