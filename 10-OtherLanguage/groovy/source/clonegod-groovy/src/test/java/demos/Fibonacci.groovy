package demos
current = 1 
next = 1 
10.times { //循环10次 
	print current + ' ' 
	newCurrent = next 
	next = next + current 
	current = newCurrent 
} 
println ''