package part1_basic

// groovy结构控制

//在一行的if语句 
if (false) assert false 


//null表示false 
if (null){ 
	assert false 
} else{ 
	assert true 
} 


//典型的while 
def i = 0 
while (i < 10) { i++ } 
assert i == 10 


//迭代一个range 
def clinks = 0 
for (remainingGuests in 0..9) { 
	clinks += remainingGuests 
} 
assert clinks == (10*9)/2 


//迭代一个列表
 def list = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] 
 for (j in list) { 
	 assert j == list[j] 
 } 
 
 
//以闭包为参数的each方法 
list.each( { item -> 
	print item
	assert item == list[item] 
})


//典型的switch 
switch(3) { 
	case 1 : assert false; break 
	case 3 : assert true; break 
	default: assert false 
}



