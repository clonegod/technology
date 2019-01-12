package part1_basic

// 代码块：闭包 - 通常用在函数式语言中，它允许执行一个任意指定好的代码块。

// 闭包中实际上就是将一小段程序封装起来，以完成某个特定的目的。在使用时，将参数传入这个闭包，然后闭包会返回结果
// java 中的匿名类，比如FilenameFilter接口所起到的作用
// java8 中的lambda表达式，也可以看做是闭包的一种实现



// List.each方法需要一个简单的参数——闭包，然后遍历list的元素，每次遍历时将当前的元素传递给闭包，有多少个元素，闭包就被调用多少次。
[1,2,3].each {ele -> println ele}


// 如果n个人在会议室，并且每两个人之间进行碰杯，有多少次碰杯？
// 算法：在一个人员到达会议室之后，这个人和已经到达会议室的每个人进行碰杯，通过这个方法，每一个人都将与会议室的所有人碰杯。
// 使用闭包计算所有在会议室的客人之间的碰杯数()
def totalClinks = 0 
def partyPeople = 100 
1.upto(partyPeople) { guestNumber -> 
	clinksWithGuest = guestNumber-1 
	totalClinks += clinksWithGuest 
} 

//高斯算法
assert totalClinks == (partyPeople*(partyPeople-1))/2 


